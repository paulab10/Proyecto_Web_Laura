import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {UploadFileService} from "../upload-file.service";
import {FilesStatus} from "../model/filesStatus";
import {HttpEventType, HttpResponse} from "@angular/common/http";

@Component({
  moduleId: module.id,
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  filesStatus: FilesStatus;

  iconsImg = {
    detailview: "../../assets/icon_process_gray.png",
    isdp: "../../assets/icon_no_po_gray.png",
  };

  isProcessing: boolean;

  ngOnInit() {
    this.isProcessing = false;
    this.getFilesStatus();
  }

  constructor(private router: Router,
              private uploadService: UploadFileService) { }


  goToLoadFiles() {
    this.router.navigate(['load-files']);
  }

  goToDetailsSuppliers() {
    this.router.navigate(['info-details']);
  }

  goToDetailsNoPo() {
    this.router.navigate(['view-details']);
  }

  getFilesStatus() {
    this.uploadService.getFilesStatus()
      .subscribe(status => {
        if (status != null) {
          this.filesStatus = status;
          this.updateOptions();
          console.log(status);
        }
      });
  }

  private updateOptions() {

    if (this.filesStatus.detailview) {
      this.iconsImg.detailview = "../../assets/icon_process.png";
    }

    if (this.filesStatus.isdp) {
      this.iconsImg.isdp = "../../assets/icon_no_po.png";
    }

  }

  updateDetailView() {
    this.openModal();

    this.uploadService.updateDetailView().subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        console.log("Uploading..");
      }
      else if (event instanceof HttpResponse) {
        console.log("DV Updated");
        this.isProcessing = false;
        this.router.navigateByUrl(`/info-details`);
      }
    });
  }

  openModal() {
    this.isProcessing = true;
  }
}
