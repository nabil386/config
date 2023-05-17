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

<link href="../themes/previews/css/v6_main.css" rel="stylesheet" />
<link href="../themes/previews/css/curam_main.css" rel="stylesheet" />
<link href="../themes/previews/css/main-ibm-font.css" rel="stylesheet" />

<title>C�ram V7 User Interface - Modal Dialog</title>

<style type="text/css">

iframe {
    display:block;
    width: calc(%WIDTH%px - 30px);
  	height: %HEIGHT%px;
    margin-bottom: -35px;
}

table {
	width:99%;
	height:99%;
	position:absolute;
	border:0;
	padding:0;
	margin:0;
}

tr {
	width:100%;
	height:100%
}

body {
	background: #ffffff;
}

.modal-wrapper {
	position: absolute;
  border: 1px solid #cbcbcb;
	width: %WIDTH%px;
	height: calc(%HEIGHT%px + 130px);
	margin-top: 30px;
	margin-left: 30px;
}

.modal-wrapper .modal-heading {
  border-bottom: 3px solid #1d3649;
  padding: 30px 0 20px 0;
  display: block;
  height: 23px;
  margin: 0 30px;
}

.modal-wrapper .modal-heading .title {
  color: #1d3649;
  font-family: $infrastructure_main-medium_font-family;
  font-size: 18px;
  line-height: 23px;
  height: 23px;
  float: left;
  display: block;
}

.modal-wrapper .modal-heading .buttons {
  float: right;
}

</style>

</head>
	<body class="curam modal soria">
    <div class="modal-wrapper">
      <div class="modal-heading">
        <div class="title">Modal Title</div>
        <div class="buttons">
          <a href="#" class="help"><img src="../themes/curam/images/help--20-enabled.svg" /></a>
          <a href="#" class="refresh"><img src="../themes/curam/images/close--20-enabled.svg" /></a>
        </div>
      </div>
      <div class="modal-content">
        <iframe frameborder="0" id="center" src="%PAGE_ID%.content.html"></iframe>
      </div>
      <div class="actions-panel" id="modal-actions-panel" style="margin: 0 30px;">
        <div class="action-set right" style="margin-top: 20px;">
          <a href="#" class="ac first-action-control right">
            <span class="left-corner">
              <span class="right-corner">
                <span class="middle">Sample button</span>
              </span>
            </span>
          </a>
        </div>
      </div>
    </div>
	</body>
</html>