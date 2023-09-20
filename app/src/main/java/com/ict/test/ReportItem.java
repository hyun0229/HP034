package com.ict.test;

public class ReportItem {
    public String reportContent,reportDate;
    public byte[] image;
    public ReportItem(String reportContent,String reportDate, byte[] image){
        this.reportContent = reportContent;
        this.reportDate = reportDate;
        this.image =image;
    }
    public String getReportContent(){
        return reportContent;
    }
    public String getReportDate(){
        return reportDate;
    }

    public byte[] getImage() {
        return image;
    }
}
