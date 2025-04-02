import {Component, OnInit} from '@angular/core';
import {TitleService} from '../../services/title/title.service';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
  currentTitle = 'Report Collections';

  constructor(private titleService: TitleService) {}

  ngOnInit() {
    this.titleService.currentTitle$.subscribe(title => {
      this.currentTitle = title;
    });
  }
}
