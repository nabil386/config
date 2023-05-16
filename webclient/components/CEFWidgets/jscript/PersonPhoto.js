function replaceDownloadLink() {

 

  var items = document.getElementsByTagName("a");

  for(var i=0; i<items.length; i++)

  {

    if(items[i].getAttribute("href").indexOf("servlet/FileDownload") > 0)

    {

      //alert(items[i].innerHTML);

      var download = items[i];

 

      // Get the URL to the Image

      var url = download.getAttribute("href");

 

      // Retrieve the parent node

      var parent = download.parentNode;

 

      // Create the Image node

      var imgElement = document.createElement("img");

      imgElement.setAttribute("src", url);

      imgElement.setAttribute("alt", "Photo");

      imgElement.setAttribute("width", 105);

      imgElement.setAttribute("height", 75);

 

      // Repace the link with the image

      parent.replaceChild(imgElement,download);

    }

  }

 

}
