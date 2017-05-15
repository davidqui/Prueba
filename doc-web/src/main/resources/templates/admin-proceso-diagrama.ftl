<#assign pageTitle = "Diagrama de proceso" />
<#include "gen-macros.ftl">
<#if templatePrefix??>
    <#include templatePrefix + "-header.ftl">
<#else>
    <#include "header.ftl">
</#if>

<div class="container-fluid">
    <h1 class="cus-h1-page-title">${pageTitle}</h1>

    <script type="text/javascript" src="/js/vis.min.js"></script>

    <div id="mynetwork" style="width: 100%; height: 650px; border: 1px solid lightgray;"></div>

    <script src="/jquery/jquery.min.js"></script>
    <script type="text/javascript">

        $.get("/admin/proceso/proceso-json?proId=${pid}", function(data) {

          // create an array with nodes
          var nodes = data.nodes;

          // create an array with edges
          var edges = data.edges;

          // create a network
          var container = document.getElementById('mynetwork');

          // provide the data in the vis format
          var netdata = {
              nodes: data.nodes,
              edges: data.edges
          };

          // initialize your network!
          var network = new vis.Network(container, netdata, data.options);
        });
    </script>
</div>

<#if templatePrefix??>
    <#include templatePrefix + "-footer.ftl">
<#else>
    <#include "footer.ftl">
</#if>
