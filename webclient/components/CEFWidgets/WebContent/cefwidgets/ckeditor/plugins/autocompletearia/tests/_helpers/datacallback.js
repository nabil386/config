﻿"use strict";function dataCallback(e){return function(n,a,o){n=n.toLowerCase().replace(/\u00a0/g," ").substr(e&&e.length?e.length:0),setTimeout(function(){o(filter(data.persons,n))},500*Math.random())}}function dataCallbackSync(e){return function(n,a,o){n=n.toLowerCase().replace(/\u00a0/g," ").substr(e&&e.length?e.length:0),o(filter(data.persons,n))}}function filter(e,n){if(CKEDITOR.env.ie&&CKEDITOR.env.version<9){var a,o=[];for(a=0;a<e.length;a++)0===e[a].name.toLowerCase().indexOf(n)&&o.push(e[a]);return o}return e.filter(function(e){return 0===e.name.toLowerCase().indexOf(n)})}var data={totalResults:501,startIndex:1,numResultsInCurrentPage:15,persons:[{id:"8d579540-f6df-1032-9b06-d02a14283ea9",name:"Amy Jones150",userType:"EMPLOYEE",email:"ajones150@janet.iris.com",confidence:"low",score:11100},{id:"8df02bc0-f6df-1032-9b70-d02a14283ea9",name:"Amy Jones256",userType:"EMPLOYEE",email:"ajones256@janet.iris.com",confidence:"low",score:11100},{id:"d871a840-f6df-1032-9c3c-d02a14283ea9",name:"Amy Jones460",userType:"EMPLOYEE",email:"ajones460@janet.iris.com",confidence:"low",score:11100},{id:"8cbefec0-f6df-1032-9ae8-d02a14283ea9",name:"Amy Jones120",userType:"EMPLOYEE",email:"ajones120@janet.iris.com",confidence:"low",score:11100},{id:"8c266840-f6df-1032-9a8b-d02a14283ea9",name:"Amy Jones26",userType:"EMPLOYEE",email:"ajones26@janet.iris.com",confidence:"low",score:11100},{id:"8cbefec0-f6df-1032-9ab8-d02a14283ea9",name:"Amy Jones72",userType:"EMPLOYEE",email:"ajones72@janet.iris.com",confidence:"low",score:11100},{id:"8cbefec0-f6df-1032-9ad2-d02a14283ea9",name:"Amy Jones98",userType:"EMPLOYEE",email:"ajones98@janet.iris.com",confidence:"low",score:11100},{id:"8e88c240-f6df-1032-9bba-d02a14283ea9",name:"Amy Jones330",userType:"EMPLOYEE",email:"ajones330@janet.iris.com",confidence:"low",score:11100},{id:"8df02bc0-f6df-1032-9b3c-d02a14283ea9",name:"Amy Jones204",userType:"EMPLOYEE",email:"ajones204@janet.iris.com",confidence:"low",score:11100},{id:"8e88c240-f6df-1032-9bc8-d02a14283ea9",name:"Amy Jones344",userType:"EMPLOYEE",email:"ajones344@janet.iris.com",confidence:"low",score:11100},{id:"8cbefec0-f6df-1032-9aee-d02a14283ea9",name:"Amy Jones126",userType:"EMPLOYEE",email:"ajones126@janet.iris.com",confidence:"low",score:11100},{id:"d7d911c0-f6df-1032-9c28-d02a14283ea9",name:"Amy Jones440",userType:"EMPLOYEE",email:"ajones440@janet.iris.com",confidence:"low",score:11100},{id:"8df02bc0-f6df-1032-9b42-d02a14283ea9",name:"Amy Jones210",userType:"EMPLOYEE",email:"ajones210@janet.iris.com",confidence:"low",score:10725},{id:"8df02bc0-f6df-1032-9b34-d02a14283ea9",name:"Amy Jones196",userType:"EMPLOYEE",email:"ajones196@janet.iris.com",confidence:"low",score:10725},{id:"8df02bc0-f6df-1032-9b30-d02a14283ea9",name:"Amy Jones192",userType:"EMPLOYEE",email:"ajones192@janet.iris.com",confidence:"low",score:10725}]};