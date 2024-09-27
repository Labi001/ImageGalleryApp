package com.example.imagegalleryapp.repository.imageDownloader

interface Downloader {


    fun downloadFile(url:String, fileName:String?)

}