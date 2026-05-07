package com.ecosorter.dto;

public class QRCodeScanRequest {
    private String qrCode;
    private Long userId;

    public QRCodeScanRequest() {
    }

    public QRCodeScanRequest(String qrCode, Long userId) {
        this.qrCode = qrCode;
        this.userId = userId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
