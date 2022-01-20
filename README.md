<div id="top"></div>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/sollarp/Wifi-Card-Scanner">
    <img src="scaner log.png" alt="Logo" width="" height="80">
  </a>
<h3 align="center">Wifi Card Scanner</h3>
</div>

 [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#resources">Resources</a></li>
    <li><a href="#overall">Overall</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

[![Product Name Screen Shot][product-screenshot]](https://example.com)

* App takes image using device built in camera and using Text API to collect Wifi SSID and Password then create WiFi QR code. 
* Using On Device Recognition Version "text-recognition:17.0.1" or lower is FREE doesn't require FireBase registration.
* Automatically connect to WiFi network using WifiManager API.
* Manual input text capability to override incorrect or missing caracters.
<p align="right">(<a href="#top">back to top</a>)</p>


### Built With

* [Kotlin](https://kotlinlang.org/)
* [ML Kit](https://developers.google.com/ml-kit/)

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple example steps.



### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/sollarp/Wifi-Card-Scanner.git
   ```
2. Or use Github CLI
   ```sh
   gh repo clone sollarp/Wifi-Card-Scanner
   ```


<p align="right">(<a href="#top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

- App is useful for Network Engineers dealing with multiple   WiFi     access point installations where network name and password different. Then able connect users by scanning WiFi QR without manually entering network details.
- People struggling with modern devices (not technical).
<p align="right">(<a href="#top">back to top</a>)</p>

<!-- ROADMAP -->
## Roadmap

- Add feature to input SSID and Pass manualy only
- More work on code quality
- Implement this code in Flater
- Improve layer rotation

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- CONTRIBUTING -->
## Contributing

Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- RESOURCES -->
## Resources

* [Mlkit-text-recognition](https://developers.google.com/ml-kit/vision/text-recognition/android)
* [Camera runtime permission](https://developer.android.com/codelabs/android-app-permissions#2)
* [QR Code by Zxing](https://github.com/zxing/zxing)
* [WifiManager](https://developer.android.com/guide/topics/connectivity/wifi-suggest)
<p align="right">(<a href="#top">back to top</a>)</p>

<!-- OVERALL -->
## Overall

Camera API uses low quality image so Optical character recognition (OCR) accuracy way lower than any cloud API depending on size of character and brightness. Using CameraX which gives ability to control image quality should improve accuracy.


<p align="right">(<a href="#top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[contributors-url]: https://github.com/github_username/repo_name/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo_name.svg?style=for-the-badge
[forks-url]: https://github.com/github_username/repo_name/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge
[stars-url]: https://github.com/github_username/repo_name/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge
[issues-url]: https://github.com/github_username/repo_name/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo_name.svg?style=for-the-badge
[license-url]: https://github.com/github_username/repo_name/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/linkedin_username
[product-screenshot]: mdtoolpic.png
