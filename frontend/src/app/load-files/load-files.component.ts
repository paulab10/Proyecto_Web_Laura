import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {UploadFileService} from "../upload-file.service";
import {HttpEventType, HttpResponse} from "@angular/common/http";
import {FilesStatus} from "../model/filesStatus";
import {forEach} from "@angular/router/src/utils/collection";

@Component({
  selector: 'app-load-files',
  templateUrl: './load-files.component.html',
  styleUrls: ['./load-files.component.scss']
})
export class LoadFilesComponent implements OnInit {

  excelImg = {
    detailview: "../../assets/excel_gray.png",
    isdp: "../../assets/excel_gray.png",
  };

  inputEnabled = {
    detailview: true,
    isdp: true,
  };

  selectedFiles: FileList;
  currentFileUpload: File;

  filesStatus: FilesStatus;

  progress: { percentage: number } = { percentage: 0 };
  isProcessing = false;

  constructor(private uploadService: UploadFileService,
              private router: Router) { }

  ngOnInit() {
    this.getFilesStatus();
  }

  selectFile(event, fileName) {
    this.selectedFiles = event.target.files;
    this.excelImg[fileName] = "../../assets/excel_color.png";
    if (fileName === "detailview") {
      this.uploadFile(fileName);
    } else {
      this.uploadFile(fileName);
    }

  }

  uploadFile(fileName) {
    this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0);
    //this.currentFileUpload.
    this.uploadService.pushFileToStorage(this.currentFileUpload, fileName).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
        console.log(this.progress.percentage);
      } else if (event instanceof HttpResponse) {
        console.log('File is completely uploaded!');
      }
    });

    this.selectedFiles = undefined;
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

  getFilesStatus() {
    this.uploadService.getFilesStatus()
      .subscribe(status => {
        if (status != null) {
          this.filesStatus = status;
          this.updateCards();
          console.log(status);
        }
      });
  }

  updateCards() {
    this.inputEnabled.detailview = this.filesStatus.detailview;
    this.inputEnabled.isdp = this.filesStatus.isdp;

    for(let key in this.inputEnabled) {
      if (this.inputEnabled[key]) {
        this.excelImg[key] = "../../assets/icon_excel_color.png";
      }
    }
  }

}
