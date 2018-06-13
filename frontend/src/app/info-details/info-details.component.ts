import { Component, OnInit } from '@angular/core';
import {ProductExcel} from "../model/productExcel";
import {UploadFileService} from "../upload-file.service";

@Component({
  selector: 'app-info-details',
  templateUrl: './info-details.component.html',
  styleUrls: ['./info-details.component.scss']
})
export class InfoDetailsComponent implements OnInit {

  constructor(private uploadService: UploadFileService) { }

  products: ProductExcel[];

  supplierName: string;

  /* Active tabs*/
  tabActive = {
    sicte: false,
    dico: false,
    fscr: false,
    enecon: false,
    conectar: false,
    applus: false
  };

  /* Active table */
  tableActive = {
    available: false,
    create: false,
    adjust: false,
    check: false
  };


  ngOnInit() {
    this.tabActive.sicte = true;
    this.tableActive.available = true;
    this.supplierName = 'sicte';

    this.getSupplierList();
  }

  getSupplierList() {
    this.uncheckAllTables();

    this.tableActive.available = true;

    this.uploadService.getSupplierList(this.supplierName)
      .subscribe( products => {
        if (products != null) {
          this.products = products;
          console.log(products);
        }
      });
  }

  uncheckAllTabs() {
    this.tabActive.sicte = false;
    this.tabActive.applus =  false;
    this.tabActive.conectar =  false;
    this.tabActive.dico =  false;
    this.tabActive.fscr =  false;
    this.tabActive.enecon =  false;
  }

  uncheckAllTables() {
    this.tableActive.available = false;
    this.tableActive.create = false;
    this.tableActive.adjust = false;
    this.tableActive.check = false;
  }

  onTabClicked(tabName) {
    this.uncheckAllTabs();

    this.tabActive[tabName] = true;
    this.supplierName = tabName;

    this.getSupplierList();
  }

}
