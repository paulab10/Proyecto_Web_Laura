package com.huawei.fileshandlingapi.model;

public class FilesStatus {
    private boolean detailview;
    private boolean isdp;

    public FilesStatus() {
    }

    public FilesStatus(boolean detailview, boolean isdp) {

    }

    public boolean isDetailview() {
        return detailview;
    }

    public void setDetailview(boolean detailview) {
        this.detailview = detailview;
    }

    public boolean isIsdp() {
        return isdp;
    }

    public void setIsdp(boolean isdp) {
        this.isdp = isdp;
    }
}
