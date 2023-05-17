<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script type="text/javascript">
  dojoConfig = {
    packages: [
      { name: "curam", location: "../../curam" },
      { name: "cm", location: "../../cm" },
      { name: "idx", location: "../../ibmidxtk/idx" },
    ],
    parseOnLoad: true,
    async: false,
    debugAtAllCosts: true,
    locale:'en-us',
    has: {
      "dojo-debug-messages": true
    }
  };
</script>
<script type="text/javascript" src="/CDEJ/jscript/dojotk/dojo/dojo.js">// dummy script content</script>
<script type="text/javascript" src="/CDEJ/jscript/dojo.layer.js">// script content</script>
<script type="text/javascript" src="/CDEJ/jscript/cdej.js">// script content c</script>

<title>Cœram V7 User Interface</title>

<style type="text/css">
*, html, body { height: 100%; width:100%; margin:0; padding:0; overflow: hidden;}

table{
    font-family: $infrastructure_main-regular_font-family;
    font-size: 12px;
    table-layout: fixed;
    color: #325c80;
    width: 100%;
    height: 100%;
    border-collapse: collapse;
    border-spacing: 0;
}

#app-banner {
  font-family: $infrastructure_main-bold_font-family;
  font-size: 12px;
  font-weight: bold;
  height: 40px;
  background-color: #1d3649;
  line-height: 40px;
  color: #ffffff;
  text-align: center;
}

#shortcut_panel, #smart_panel {
    width: 39px;
    background-color:white;
    border: 1px solid #A7BACD;
    border-top: 0px;
    border-bottom: 0px;
}

#navigation_group {
    width: 150px;
    color: #121212;
    font-weight: bold;
    background-color:#f4f4f4;
    border: 1px solid #A7BACD;
    border-top: 0px;
    border-bottom: 0px;
}

#main_panel {
  position: relative;
}

iframe {
	width:100%;
	height: calc(100% - 40px);
	padding:0;
	border:0;
  position: absolute;
  top: 0;
}

</style>


</head>
	<body>
    <div id="app-banner">IBM Social Program Management</div>
		<table>
		  <tr>
			<td id="shortcut_panel" align="center">S<br/>h<br/>o<br/>r<br/>c<br/>u<br/>t<br/> <br/>P<br/>a<br/>n<br/>e<br/>l<br/></td>
			<td id="navigation_group" align="center">N<br/>a<br/>v<br/>i<br/>g<br/>a<br/>t<br/>i<br/>o<br/>n<br/> <br/>G<br/>r<br/>o<br/>u<br/>p</td>
			<td id="main_panel"><iframe src="%PAGE_ID%.content.html"></iframe></td>
			<td id="smart_panel" align="center">S<br/>m<br/>a<br/>r<br/>t<br/> <br/>P<br/>a<br/>n<br/>e<br/>l</td>
		  </tr>
		</table>
	</body>
</html>
