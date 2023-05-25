<html>
    <head>
        <title>Java deserialization example</title>
    </head>
    <style>
    h2 {text-align: center;}
    form {text-align: center;}
    </style>
    <body>
        <h2>Hello <%= request.getAttribute("name") %>!</h2>
        <form action="/" method="POST">
            Write your name: <input type="text" name="name" />
            <input type="submit"/>
        </form>
    </body>
</html>