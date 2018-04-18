<html>
    <head>
        <title>Firmar nuevo temporal</title>
        </head>
    <body>
        <form action="/documento/firmar-nuevo" method="GET">
            <table border="1">
                <thead>
                    <tr>
                        <th>Parámetro</th>
                        <th>Valor</th>
                        </tr>
                    </thead>
                <tbody>
                    <tr>
                        <td>PIN</td>
                        <td><input type="text" id="pin" name="pin" value="64b4215af76d4b6581fed325796f9ee4"/></td>
                        </tr>
                    <tr>
                        <td>TID</td>
                        <td><input type="number" id="tid" name="tid" value="17"/></td>
                        </tr>
                    </tbody>
                </table>
            <input type="submit" />
            </form>
        </body>
    </html>