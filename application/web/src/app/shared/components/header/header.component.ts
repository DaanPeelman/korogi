import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'korogi-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  transparent: boolean;

  constructor() { }

  ngOnInit() {
    this.transparent = true;
  }

}