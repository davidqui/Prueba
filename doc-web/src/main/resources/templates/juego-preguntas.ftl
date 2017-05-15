    <#assign pageTitle = "Procesos">
    <#assign activePill = "proceso">
    <#include "header.ftl">
    <div class="container-fluid">
        <div class="jumbotron cus-juego-jumbotron">
            <div class="preguntas-div">
                <p id="juegoId" hidden>${jjuId}</p>
                <p id="juegoPreguntaId" hidden>${pregunta.id}</p>
                <p id="nPregunta" hidden>${numPregunta}</p>
                <p id="ayuda-1" hidden>${primeraAyuda}</p> 
                <p id="ayuda-2" hidden>${segundaAyuda}</p> 
                <p id="juegoNivelId" hidden>${jniId}</p> 
                <div class="row">  
                    <div class="col-md-8">

                        <div id="j-tiempo-wrapper">
                           
                            <div class="label cus-label">
                                <div class="time"></div>
                            </div>
                        </div>
                        <div class="j-respuesta-wrapper">
                            <div id="j-pregunta">
                                <div id="j-pregunta-fragment-1"></div>
                                <div id="j-pregunta-fragment-2"></div>
                                <h5 class="juego-pregunta"><span class="juego-pregunta">${pregunta.textoPregunta}</span></h5>
                            </div>
                        </div>

                        <div class="row">
                            <#assign x=0>
                            <#list pregunta.respuestas as r>
                            <#assign x = x + 1>
                            <div class="col-md-6">
                                <a id="respuesta${x}" class="juego-r" href="/capacitacion-juego/respuesta?jjuId=${jjuId}&jrpId=${r.id}&numPregunta=${numPregunta}&preguntaId=${preguntaId}">
                                  <div class="j-respuesta-wrapper" id="bgRespuesta1${x}">
                                    <div class="j-respuesta" id="bgRespuesta2${x}">
                                        <div class="j-respuesta-fragment-1" id="bgRespuesta3${x}"></div>
                                        <div class="j-respuesta-fragment-2" id="bgRespuesta4${x}"></div>
                                        <p class="respuesta-link">
                                            <span id="bgRespuesta5${x}" class="respuesta-link">

                                                <span>${r.textoRespuesta}</span>

                                            </span>
                                        </p>
                                        <div class="totalP">
                                        <p id="porcentaje${x}" class="totalP"></p>
                                        </div>
                                        <p id="respuestaId${x}" hidden>${r.id}</p>
                                        <p id="numRespuesta${x}" hidden>${x}</p>
                                        <p id="esCorrecta${x}" hidden>${r.esCorrecta?string}</p>

                                    </div>
                                </div>

                            </div>
                            </#list>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div id="help1">
                            <a class="help1" id="cincuenta">
                                <div id="ayuda1" class="btn"></div>
                                <p class="help1">Ayuda 1: Elimina 2 respuestas </p>
                            </a>
                        </div>
                        <div class="help2">
                            <a class="help2" id="laMas">
                                <div id="ayuda2" class="btn"></div>
                                <p class="help2">Ayuda 2: Muestra el porcentaje de selección</p>
                            </a>
                        </div>

                    </div>

                </div>
            </div>
        </div>
    </div>
        <script>
            $(document).ready(function(){

                if($("#ayuda-1").text()==1){
                 $('.help1').hide();
                 $('#ayuda1').hide();
             }
             if($("#ayuda-2").text()==1){
                $('.help2').hide();
                $('#ayuda2').hide();
            }

            $("#cincuenta").click(function(){
                $('.help1').hide(); 
                var r= (Math.floor(Math.random() *4) + 1);
                var r2= (Math.floor(Math.random() *4) + 1);
                var laEliminada= 0;
                var numR= 0;
                var arr = [ 1, 2,3,4 ];

      // Para descartar la primera respuesta al azar
      jQuery.each( arr, function( i, l ) {

          if ($('#esCorrecta'+l).text() == 'true'&&r==$( '#numRespuesta' + l ).text()){
            r= (Math.floor(Math.random() *4) + 1);
            l=1;
            i=0;
        }else if ($('#esCorrecta'+l).text() == 'false'){
            $('#respuesta'+l).hide();
            $('#bgRespuesta5'+l).hide();
            laEliminada=l;
            return (false);
        }
    });
      // Para descartar la segunda respuesta al azar
      jQuery.each( arr, function( i, l ) {

          if(laEliminada!=l){
            if ($('#esCorrecta'+l).text() == 'true'&&r2==$( '#numRespuesta' + l ).text()&&laEliminada!=l){
                r2= (Math.floor(Math.random() *4) + 1);
                l=1;
                i=0;
            }else if ($('#esCorrecta'+l).text() == 'false'){
                $('#respuesta'+l).hide();
                $('#numRespuesta'+l).hide();
                $('#bgRespuesta5'+l).hide();
                return (false);
            }
        }

    });
      
      $.get('/capacitacion-juego/juego-ayuda-cincuenta-cincuenta', {

        jniId : $("#juegoNivelId").text(),
    }, function(data) {


    });
      
    });


    //Countdown
    var startValue = 30000; //Number of milliseconds
    var time = new Date(startValue);
    var interv;


    function done(){

        window.location = "/capacitacion-juego/puntuacion/${jjuId}/-1";

    }
    function readyFn(jQuery){
        displayTime();
        $(function(){
            interv = setInterval(function(){
                time = new Date(time - 1000);
                if(time<=0){
                    done();
                    clearInterval(interv);
                }
                displayTime();
            }, 1000);
        });
        $(".stop").on("click", function(){
            clearInterval(interv);
            time = new Date(startValue);
            displayTime();
        });

    }

    function displayTime(){
        $(".time").text(fillZeroes(time.getSeconds()));
    }

    function fillZeroes(t){
        t = t+"";
        if(t.length==1)
            return "0" + t;
        else
            return t;
    }

    $( window ).load( readyFn );   

    //Sombrea la respuesta correcta
    $(".juego-r").click(function(){

        var arr = [ 1, 2,3,4 ];

        jQuery.each( arr, function( i, l ) {

          if ($('#esCorrecta'+l).text() == 'true'){

              $('#bgRespuesta2'+l).css({"background": "#f1c232","opacity": "0.4"});

          }
      });
    });
    //Ayuda del público

    $("#laMas").click(function(){
        $('#ayuda2').hide(); 
        $('.help2').hide(); 

        $.get('/capacitacion-juego/juego-ayuda-publico', {
            jprId : $("#juegoPreguntaId").text(),
            jniId : $("#juegoNivelId").text(),
        }, function(data) {

            var arr = [ 1, 2,3,4 ];
            // Para poner los porcentajes
            jQuery.each( arr, function( i, l ) {
                jQuery.each( data, function( key, value ) {

                    if ($( '#respuestaId' + l ).text()==key){
                        $('#porcentaje'+l).append(value+"%");
                        
                    }
                });
            });

            // Para poner 0 en los que no tiene porcentaje
            jQuery.each( arr, function( i, l ) {
                if ($('#porcentaje'+l).text()==''){
                    $('#porcentaje'+l).append(0+"%");

                }

            });
            
        });

    });   
    });


    </script>
<#include "admin-footer.ftl">