</div>
</div>
<!-- jQuery first, then Bootstrap JS. -->
<script src="/jquery/jquery.min.js"></script>
<script src="/js/jquery-ui.min.js"></script>
<script src="/js/tether.min.js"></script>
<script src="/js/bootstrap.js"></script>
<script type="text/javascript" src="/js/typeahead.bundle.js"></script>

<script type="text/javascript">
  $(function () {
    $('[data-toggle="popover"]').popover()
  });

  $('.popover-dismiss').popover({
    trigger: 'focus'
  });

  $(".datepicker").datepicker({dateFormat:'yy-mm-dd'});
  $(".datepicker").each(function(){
    var date = $(this).attr('value');
    $(this).datepicker('setDate', date);
  });
    </script>

    ${deferredJS!""}

    <#--
        2018-04-26 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech) 
        feature-151: Javascript general para operaciones con el framework.
    -->
<script src="/js/app/framework-util.js"></script>
<#if usuActivo??>
    <#if usuActivo>
    <#else>
        <script>
            jQuery('#masterdiv').html('');
        </script>   
    </#if>
</#if>
</body>
</html>