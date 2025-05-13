package com.example.Boilerplate_JWTBasedAuthentication.controller;

import com.example.Boilerplate_JWTBasedAuthentication.dto.common.RestResponse;
import com.example.Boilerplate_JWTBasedAuthentication.dto.response.CVAnalysisResponse;
import com.example.Boilerplate_JWTBasedAuthentication.service.NovitaAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("api/ai")
@RequiredArgsConstructor
public class ReviewCVController {

    private final NovitaAIService novitaAIService;

    @PostMapping("/review-cv")
    public ResponseEntity<RestResponse<String>> analyzeCV(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(RestResponse.error(HttpStatus.BAD_REQUEST.value(), "File is empty", null));
        }

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            return ResponseEntity.badRequest()
                    .body(RestResponse.error(HttpStatus.BAD_REQUEST.value(), "Only PDF files are allowed", null));
        }

        File tempFile = null;
        try {
            // Tạo thư mục temp nếu chưa tồn tại
            Path tempDir = Files.createTempDirectory("cv_uploads");
            log.info("Created temporary directory at: {}", tempDir.toAbsolutePath());
            
            tempFile = File.createTempFile("upload_", ".pdf", tempDir.toFile());
            file.transferTo(tempFile);
            
            log.info("Temporary file created at: {}", tempFile.getAbsolutePath());

            // Lấy đường dẫn tuyệt đối đến file Python script
            String projectDir = System.getProperty("user.dir");
            Path pythonScriptPath = Paths.get(projectDir, "src", "main", "java", "com", "example", 
                    "Boilerplate_JWTBasedAuthentication", "tool", "extract.py");

            if (!Files.exists(pythonScriptPath)) {
                log.error("Python script not found at: {}", pythonScriptPath);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                                "Python script not found", null));
            }

            log.info("Python script found at: {}", pythonScriptPath);


            String pythonCommand = "python";

            try {
                Process checkPython = new ProcessBuilder("python", "--version")
                        .redirectErrorStream(true).start();
                int exitCode = checkPython.waitFor();

                // Nếu lỗi hoặc exitCode khác 0 thì fallback sang đường dẫn tuyệt đối
                if (exitCode != 0) {
                    throw new IOException("python not found");
                }

                log.info("Using system 'python' command.");
            } catch (IOException | InterruptedException ex) {
                // fallback
                pythonCommand = "C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python313\\python.exe";
                log.warn("System 'python' not available, fallback to: {}", pythonCommand);
            }

            // Gọi Python script xử lý file PDF
            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonCommand,
                    pythonScriptPath.toString(),
                    tempFile.getAbsolutePath()
            );

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Đọc kết quả trả về từ Python
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            StringBuilder extractedText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                extractedText.append(line).append("\n");
            }

            // Đợi process hoàn thành và kiểm tra exit code
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("Python script failed with exit code: {}", exitCode);
                log.error("Error output: {}", extractedText.toString());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                                "Failed to process PDF file: " + extractedText.toString(), null));
            }

            log.info("Successfully extracted text from PDF");

            // Gọi Novita AI để phân tích CV
            String analysis = novitaAIService.analyzeCV(extractedText.toString());
            
            // Tạo CVAnalysisResponse và lấy markdown content
            CVAnalysisResponse cvAnalysisResponse = new CVAnalysisResponse(analysis);
            String markdownContent = cvAnalysisResponse.getMarkdownContent();

            return ResponseEntity.ok(
                    RestResponse.success(markdownContent, "Successfully analyzed CV")
            );

        } catch (IOException | InterruptedException e) {
            log.error("Error processing PDF file: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                            "Error processing PDF file: " + e.getMessage(), null));
        } finally {
            // Cleanup: xóa file tạm và thư mục tạm
            if (tempFile != null && tempFile.exists()) {
                boolean deleted = tempFile.delete();
                if (!deleted) {
                    log.warn("Failed to delete temporary file: {}", tempFile.getAbsolutePath());
                }
                // Xóa thư mục tạm
                try {
                    Files.deleteIfExists(tempFile.getParentFile().toPath());
                    log.info("Deleted temporary directory: {}", tempFile.getParentFile().getAbsolutePath());
                } catch (IOException e) {
                    log.warn("Failed to delete temporary directory: {}", tempFile.getParentFile().getAbsolutePath());
                }
            }
        }
    }
}
