# Digital Asset Based Document Tamper Detection (BGV)

## Setup
1. Install Java 21, Maven, Docker
2. Configure PostgreSQL or use H2 for dev
3. Set GCS bucket in `application.yml`
4. Run: `mvn clean package`
5. Start: `java -jar target/bgv-tamper-detection-1.0.0.jar`

## APIs
- POST /api/v1/documents/upload
- GET /api/v1/documents/{documentId}/status
- GET /api/v1/documents/{documentId}/metadata
- GET /api/v1/documents/{documentId}/ocr-validation
- GET /api/v1/documents/{documentId}/tamper-analysis
- GET /api/v1/documents/{documentId}/risk-score
- GET /api/v1/documents/{documentId}/audit-trail
- GET /api/v1/documents
