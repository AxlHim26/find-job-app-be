# extract_text.py
import fitz  # PyMuPDF
import sys
import io

# Set console encoding to UTF-8
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

def extract_text(pdf_path):
    try:
        doc = fitz.open(pdf_path)
        text = ""
        for page in doc:
            text += page.get_text()
        print(text)  # In ra stdout để Spring Boot đọc được
    except Exception as e:
        print(f"Error: {str(e)}", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python extract.py <pdf_file_path>", file=sys.stderr)
        sys.exit(1)
    
    pdf_path = sys.argv[1]
    extract_text(pdf_path)
