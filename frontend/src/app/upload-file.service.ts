import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {catchError} from "rxjs/operators";
import {ProductExcel} from "./model/productExcel";
import {of} from "rxjs/observable/of";
import {FilesStatus} from "./model/filesStatus";

@Injectable()
export class UploadFileService {

  constructor(private http: HttpClient) { }

  pushFileToStorage(file: File, name: string): Observable<HttpEvent<{}>> {
    const formdata: FormData = new FormData();

    formdata.append('file', file);

    const req = new HttpRequest('POST', '/upload-file/'+ name, formdata, {
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req);
  }

  getFilesStatus(): Observable<FilesStatus> {
    return this.http.get<FilesStatus>(`/get/status`)
      .pipe(
        catchError(this.handleError<FilesStatus>('getFilesStatus'))
      );
  }

  /** Get List with info for a specific supplier */
  getSupplierList(supplier: string): Observable<ProductExcel[]> {
    return this.http.get<ProductExcel[]>(`/get/supplier/${supplier}`)
      .pipe(
        catchError(this.handleError<ProductExcel[]>('getSupplierList'))
      );
  }

  /** Get List with info for a specific supplier */
  getNoPoList(): Observable<ProductExcel[]> {
    return this.http.get<ProductExcel[]>(`/get/no-po/no_po`)
      .pipe(
        catchError(this.handleError<ProductExcel[]>('getNoPoList'))
      );
  }

  /** Get List with info for a specific supplier */
  getNearToCreateList(): Observable<ProductExcel[]> {
    return this.http.get<ProductExcel[]>(`/get/no-po/near_to_create`)
      .pipe(
        catchError(this.handleError<ProductExcel[]>('getNearToCreateList'))
      );
  }

  /** Update Detail View **/
  updateDetailView(): Observable<HttpEvent<{}>> {
    const req = new HttpRequest('PUT', '/update-dv/',"", {
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req);
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead
      // TODO: better job of transforming error for user consumption
      // this.log(`${operation} failed: ${error.message}`);
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

}
