package com.ecosorter.dto;

public class QRCodeSessionResponse {
    private String qrCode;
    private String status;
    private String message;

    public QRCodeSessionResponse() {
    }

    public QRCodeSessionResponse(String qrCode, String status) {
        this.qrCode = qrCode;
        this.status = status;
    }

    public QRCodeSessionResponse(String qrCode, String status, String message) {
        this.qrCode = qrCode;
        this.status = status;
        this.message = message;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
