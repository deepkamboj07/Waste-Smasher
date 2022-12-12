const express = require('express');
const bodyparser= require("body-parser");
const app= express();


const hostname = 'localhost';
const port = 3000;

app.set('view engine', 'ejs');
app.use(bodyparser.urlencoded({extended:true}));

app.use(express.static(__dirname + "/public"));

app.get("/",function(req,res){

  res.render("index.ejs")
});
app.get("/organic-req",function(re,res){
  res.render("organic_req.ejs")
});
app.get("/inorganic-req",function(re,res){
  res.render("inorganic_req.ejs")
});


app.listen(port, hostname, () => {
    console.log(`Server of waste smesher running at http://${hostname}:${port}/`);
  });