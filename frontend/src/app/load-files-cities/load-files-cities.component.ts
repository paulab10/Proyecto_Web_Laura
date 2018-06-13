import { Component, OnInit } from '@angular/core';
import {UploadFileService} from "../upload-file.service";
import {Router} from "@angular/router";
import {HttpEventType, HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-load-files-cities',
  templateUrl: './load-files-cities.component.html',
  styleUrls: ['./load-files-cities.component.scss']
})
export class LoadFilesCitiesComponent implements OnInit {

  excelImg = {
    detailview: "../../assets/excel_gray.png",
    sicte: "../../assets/excel_gray.png",
    fscr: "../../assets/excel_gray.png",
    enecon: "../../assets/excel_gray.png",
    applus: "../../assets/excel_gray.png",
    conectar: "../../assets/excel_gray.png",
    dico: "../../assets/excel_gray.png",
  };

  selectedFiles: FileList;
  currentFileUpload: File;
  progress: { percentage: number } = { percentage: 0 };
  isProcessing = false;

  constructor(private uploadService: UploadFileService,
              private router: Router) { }

  ngOnInit() {
  }

  selectFile(event, fileName) {
    this.selectedFiles = event.target.files;
    this.excelImg[fileName] = "../../assets/excel_color.png";
    this.uploadFile(fileName);
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
    this.isProcessing = true;
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
