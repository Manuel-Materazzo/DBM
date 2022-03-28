import { Component, OnInit, Output, Input, EventEmitter } from '@angular/core';
import { AudioPlyerOptions } from '../audioPlayer';

@Component({
  selector: 'ang-music-player',
  templateUrl: './ang-music-player.component.html',
  styleUrls: ['./ang-music-player.component.scss']
})
export class AngMusicPlayerComponent extends AudioPlyerOptions implements OnInit {
  @Input() audioList = [];
  @Input() next = true;
  @Input() previous = true;
  @Input() repeat = true;
  @Input() scrollTitle = false;
  @Output() nextEvent = new EventEmitter();
  @Output() previousEvent = new EventEmitter();
  @Output() repeatEvent = new EventEmitter();
  @Output() seekEvent = new EventEmitter();

  selectedAudio;
  currentAudioIndex = 0;
  repeatActive = false;
  isError = false;
  volumeBeforeMute;

  constructor() {
    super();
  }

  ngOnInit() {
    this.options();
    this.initiateAudioPlayer();
    this.isAudioEnded.subscribe(data => {
      if (!this.isRepeat && this.audioList.length > 0) {
        this.nextAudio();
      }
    })
  }

  nextAudio() {
    if (this.audioList.length - 1 != this.currentAudioIndex) {
      this.currentAudioIndex += 1;
      this.selectedAudio = this.audioList[this.currentAudioIndex];
      this.getAudioLength();
      if (this.isAudioPlaying) {
        this.play();
      }
      this.nextEvent.emit();
    }else{
      this.pause();
    }
  }

  previousAudio() {
    if (this.currentAudioIndex != 0) {
      this.currentAudioIndex -= 1;
      this.selectedAudio = this.audioList[this.currentAudioIndex];
      this.getAudioLength();
      if (this.isAudioPlaying) {
        this.play();
      }
      this.previousEvent.emit();
    }
  }

  seekAudio(seekAudioValue) {
    if (this.audioVolume != 0) {
      this.isMute = false;
    }
    this.audioPlayer.nativeElement.currentTime = seekAudioValue.target.value;
    this.seekEvent.emit();
  }

  repeatAudio() {
    this.isRepeat = !this.isRepeat;
    this.repeatActive = !this.repeatActive;
    this.audioPlayer.nativeElement.loop = this.isRepeat;
    this.repeatEvent.emit();
  }

  volumeChange(volume) {
    this.audioPlayer.nativeElement.volume = volume.target.value / 100;
  }

  muteAudio() {
    if (this.isMute) {
      this.audioPlayer.nativeElement.volume = 0.5;
      this.isMute = false;
    } else {
      this.audioPlayer.nativeElement.volume = 0;
      this.isMute = true;
    }
  }

  initiateAudioPlayer() {
    if (this.audioList.length <= 0) {
      this.isError = true;
    } else {
      this.selectedAudio = this.audioList[this.currentAudioIndex];
    }
  }
}
