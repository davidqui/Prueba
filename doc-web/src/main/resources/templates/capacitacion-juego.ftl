<#setting number_format="computer">
<#assign pageTitle = "Manual de Usuario SICDI">
<#assign pageTitle = descriptor.label />
<#include "gen-macros.ftl">
<#include "gen-paginacion.ftl">
<#include "header.ftl">


<!DOCTYPE HTML>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" href="/css/qqss.css" type="text/css" />

        <title>QUIEN QUIERE SER SABIO</title>

        <!--<script type="text/javascript" language="JavaScript" src="js/soundmanager2-nodebug-jsmin.js"></script>-->
        <script type="text/javascript">



        soundManager.url = 'swf/';
        soundManager.debugMode = false;
        soundManager.onload = function() {
          // SM2 has loaded - now you can create and play sounds!
          soundManager.createSound('introplay','sounds/intro_lets_play.mp3');
          soundManager.createSound('question','sounds/question.mp3');
          soundManager.createSound('question2','sounds/question2.mp3');
          soundManager.createSound('bible','sounds/bible.mp3');
          soundManager.createSound('lose','sounds/lose.mp3');
          soundManager.createSound('win','sounds/win.mp3');
          soundManager.createSound('final','sounds/final.mp3');
          soundManager.createSound('play','sounds/lets_play.mp3');  
          soundManager.createSound('intro','sounds/introduction.mp3');
          soundManager.createSound('bye','sounds/bye_contestant.mp3');
          soundManager.createSound('winner','sounds/winner.mp3');
        };
            </script>

        </head>

    <body>
        <#if descriptor.description??>
        <p class="lead">${descriptor.description}</p>
    </#if>

        <div class="game-history" style="font-size: 11px; background-color: #EFEFEF; padding: 0 5px; display: none"></div>

<!-- <div id="session" style="clear: both; color: #FFF; border: 1px solid red; width: 300px;"></div> -->

        <div class="game-wrapper">

            <div id="logs" style=" display: none; margin: 3px; padding: 5px; width: auto; border: 2px solid green; color: #FFF"></div>

            <div id="game">

                <div id="header">

    <div class="logo"></div><!-- /.logo -->

                    <div class="comodines">

                        <ul>
                            <li id="comd_cincuenta"><a href="#"></a></li>    
                            <li id="comd_publico"><a href="#"></a></li>    
                            <li id="comd_biblia"><a href="#"></a></li>
                            <li id="retirarse"><a href="#"></a></li>      
                            </ul>

                    </div><!-- /.comodines -->

                  </div><!-- /#header -->

                <div class="playground">

                    <p id="question">
                        <span  class="">----</span>
                    </p><!-- /.question -->

                    <ul id="answers">

                        <li>
                            <a href="#">
                                <span class="bullet">A:</span> 
                                <span class="answer-text"></span>
                                </a>
                            </li>

                        <li>
                            <a href="#">
                                <span class="bullet">B:</span> 
                                <span class="answer-text"></span>
                                </a>
                            </li>

                        <li>
                            <a href="#">
                                <span class="bullet">C:</span> 
                                <span class="answer-text"></span>
                                </a>
                            </li>

                        <li>
                            <a href="#">
                                <span class="bullet">D:</span> 
                                <span class="answer-text"></span>
                                </a>
                            </li>   

</ul><!-- /.answers -->

                    <div id="action">
                        <a id="action-link" data-action="reveal" href="#">Revelar respuesta</a>            
                        </div>

</div><!-- /#playground -->

</div><!-- /#game -->

            <div id="aside">

                <ul>
                    <li><em>100.000</em></li>
                    <li>50.000</li>
                    <li>25.000</li>
                    <li>12.500</li>
                    <li>6.400</li>
                    <li><em>3.200</em></li>
                    <li>1.600</li>
                    <li>800</li>
                    <li>400</li>
                    <li>200</li>
                    <li><em>100</em></li>
                    <li>50</li>
                    <li>30</li>
                    <li>20</li>
                    <li class="up">10</li>
                    </ul>



</div><!-- /#aside -->

            <a href="#" id="reset"></a>

</div><!-- /.game-wrapper -->

        <div id="dialog_comd_biblia" class="window">
            <h2>Comodín Bíblico</h2>    
            <span id="timer">--</span>    
            <p>Dispone de <strong>30</strong> segundos para hacer uso de su pista.</p>
            <p>Podrá utilizar la Biblia o el Libro indicado deacuerdo a la pista.</p>
            <h3>PISTA:</h3>
            <em id="hint">
                PREPARATE PORQUE YA VIENE LA PISTA</em>
            <a href="#"class="close">Cerrar ventana</a>
        </div><!-- /#dialog_comd_biblia -->

        <div id="dialog_comd_publico" class="window">
            <h2>Comodín del Público</h2>
            <p>Pida a los participantes que levanten la mano votando una de las cuatro posibles respuestas a la pregunta.</p>
            <a href="#"class="close">Cerrar ventana</a>
        </div><!-- /#dialog_comd_publico -->

        <div id="dialog_winner" class="window">
            <h2>Felicidades es usted un Sabio de la Biblia</h2>
            <p>El jugador ha contestado correctamente todas las <strong>15</strong> pregutnas y ha obtenido los <strong>100.000</strong> puntos por lo tanto ha ganado el juego:<br><br>
                <em>Eres un SABIO y MASTER de la BIBLIA, no muchos han logrado llegar hasta este punto.<br>La Sociedad de Jóvenes de la Iglesia la Aurora
                    se complace en otorgarle el premio mayor por su esfuerzo y estudio de la Palabra de Dios. <br>Que Dios le siga Bendiciendo!</em></p>    
            <a href="#"class="close">Cerrar ventana</a>
        </div><!-- /#dialog_winner -->

        <div id="dialog_loose" class="window">
            <h2>Respuesta Incorrecta</h2>
            <p>El jugador ha contestado incorrectamente por lo tanto sale del juego:<br><br>
                <em>Le instamos a estudiar más su Biblia para la próxima ocación, Bendiciones!</em></p>    
            <p><strong>Preguntas acertadas: <span class="total_answers">0</span></strong></p>
            <p><strong>Puntos objetinos: <span class="points">0</span></strong></p>
            <a href="index.html"class="close">Próximo participante</a> <br> <a href="#"class="close">Terminar juego</a>
        </div><!-- /#dialog_loose -->

        <div id="dialog_retirarse" class="window">
            <h2>Retirada</h2>
            <p>El jugador ha decidido retirarse del juego con el siguiente puntaje:</p>
            <p><strong>Preguntas acertadas: <span class="total_answers">0</span></strong></p>
            <p><strong>Puntos objetinos: <span class="points">0</span></strong></p>
            <a href="#"class="close">Cerrar ventana</a>
        </div><!-- /#dialog_retirarse -->

        <div id="dialog_start" style="text-align: center;" class="window">
                <div class="logo" style="margin: 5px auto; float: none;"></div><!-- /.logo -->
            <p>Producido por: <br /><strong>Sociedad de Jóvenes <br />Iglesia Adventista <br />La Aurora De Heredia</strong><br>Año 2009</p>
            <a href="#"class="start-game">EMPEZAR JUEGO</a>
        </div><!-- /#dialog_retirarse -->



<!-- Mask to cover the whole screen -->
        <div id="mask"></div>


<!-- Mask to cover the whole screen -->
        <div id="mask"></div>
        <script type="text/javascript" language="JavaScript" src="js/jquery-1.4.4.min.js"></script>
        <script type="text/javascript" language="JavaScript" src="js/pubsub.js"></script>
        <script type="text/javascript" language="JavaScript" src="js/json2.js"></script>
        <script type="text/javascript" language="JavaScript" src="js/jquery.store.js"></script>
        <script type="text/javascript" language="JavaScript" src="js/qqss.js"></script>

        <script>



            </script>
        </body>
    </html>
<#include "admin-footer.ftl">