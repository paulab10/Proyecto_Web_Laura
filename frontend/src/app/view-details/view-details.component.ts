import { Component, OnInit } from '@angular/core';
import {ProductExcel} from "../model/productExcel";
import {UploadFileService} from "../upload-file.service";

@Component({
  selector: 'app-view-details',
  templateUrl: './view-details.component.html',
  styleUrls: ['./view-details.component.scss']
})
export class ViewDetailsComponent implements OnInit {

  supplierActive: boolean;
  citiesActive: boolean;

  products: ProductExcel[];

  constructor(private uploadService: UploadFileService) { }

  ngOnInit() {
    this.getSupplierList();
  }

  getSupplierList() {
    //this.uncheckAllTables();

    //this.tableActive.available = true;

    this.uploadService.getNearToCreateList()
      .subscribe( products => {
        if (products != null) {
          this.products = products;
          console.log(products);
        }
      });
  }

}
