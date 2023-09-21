package com.ict.test;

import android.net.Uri;

public class ReportItem {
    public String reportContent,reportDate;
    public Uri image;
    public ReportItem(String reportContent,String reportDate, Uri image){
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

    public Uri getImage() {
        return image;
    }
}
