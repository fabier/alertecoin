<html>

<head>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <title><g:layoutTitle default='User Registration'/></title>

    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>

    <s2ui:resources module='register'/>

    <link rel="apple-touch-icon" sizes="57x57" href="${assetPath(src: 'favicons/apple-icon-57x57.png')}">
    <link rel="apple-touch-icon" sizes="60x60" href="${assetPath(src: 'favicons/apple-icon-60x60.png')}">
    <link rel="apple-touch-icon" sizes="72x72" href="${assetPath(src: 'favicons/apple-icon-72x72.png')}">
    <link rel="apple-touch-icon" sizes="76x76" href="${assetPath(src: 'favicons/apple-icon-76x76.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'favicons/apple-icon-114x114.png')}">
    <link rel="apple-touch-icon" sizes="120x120" href="${assetPath(src: 'favicons/apple-icon-120x120.png')}">
    <link rel="apple-touch-icon" sizes="144x144" href="${assetPath(src: 'favicons/apple-icon-144x144.png')}">
    <link rel="apple-touch-icon" sizes="152x152" href="${assetPath(src: 'favicons/apple-icon-152x152.png')}">
    <link rel="apple-touch-icon" sizes="180x180" href="${assetPath(src: 'favicons/apple-icon-180x180.png')}">
    <link rel="icon" type="image/png" sizes="192x192" href="${assetPath(src: 'favicons/android-icon-192x192.png')}">
    <link rel="icon" type="image/png" sizes="32x32" href="${assetPath(src: 'favicons/favicon-32x32.png')}">
    <link rel="icon" type="image/png" sizes="96x96" href="${assetPath(src: 'favicons/favicon-96x96.png')}">
    <link rel="icon" type="image/png" sizes="16x16" href="${assetPath(src: 'favicons/favicon-16x16.png')}">
    <link rel="manifest" href="${assetPath(src: 'favicons/manifest.json')}">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="msapplication-TileImage" content="/ms-icon-144x144.png">
    <meta name="theme-color" content="#ffffff">

    <g:layoutHead/>
    <ga:trackPageview/>
</head>

<body>

<s2ui:layoutResources module='register'/>
<g:layoutBody/>
<%--
<g:javascript src='jquery/jquery.jgrowl.js' plugin='spring-security-ui'/>
<g:javascript src='jquery/jquery.checkbox.js' plugin='spring-security-ui'/>
<g:javascript src='spring-security-ui.js' plugin='spring-security-ui'/>
--%>

<s2ui:showFlash/>

</body>
</html>
