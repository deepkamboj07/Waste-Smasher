import { initializeApp } from "https://www.gstatic.com/firebasejs/9.4.0/firebase-app.js";
import { getDatabase, ref, child, get ,onValue,set,remove} from "https://www.gstatic.com/firebasejs/9.4.0/firebase-database.js";
import { getAuth } from "https://www.gstatic.com/firebasejs/9.4.0/firebase-auth.js";
const firebaseConfig = {
  apiKey: "AIzaSyA8vhYRHXcBwTtn-AytqHNrmCFyUjq8Euc",
  authDomain: "waste-managment-e5c8a.firebaseapp.com",
  databaseURL: "https://waste-managment-e5c8a-default-rtdb.firebaseio.com",
  projectId: "waste-managment-e5c8a",
  storageBucket: "waste-managment-e5c8a.appspot.com",
  messagingSenderId: "704321136469",
  appId: "1:704321136469:web:82d737638b07d399be71dc",
  measurementId: "G-Q6CPRVM9J8"
};

const app = initializeApp(firebaseConfig);

// Get a reference to the database service
const database = getDatabase(app);

function Progress(id){
    console.log("click");
}

const dbRef = ref(getDatabase());
    get(child(dbRef, 'OrganicRequest/')).then((snapshot) => {
    if (snapshot.exists()) {
      
        const data=snapshot.toJSON();
        const length=Object.keys(data).length;
        let colStatus="black";
        
        for (const key in data) {
            const parent=document.getElementById("request-container");
            const div = document.createElement("div");
            div.classList.add("request-row");
            let status=data[key].status;
            if(status=="Request Send")
            {
                status="Request Recived";
                colStatus="#292b2c";
            }
            else if(status=="On Progress..")
            {
                colStatus="#0099CC";
            }
            else if(status=="Waste Collected..")
            {
                colStatus="#008000";
            }
            let content=`<img class="organic-img" src="${data[key].postImg}"</img>
            <div class="text-content">
                <h3>Waste Type : ${data[key].type}</h3>
                <p>Waste Address : ${data[key].address}</p>
                <p>Waste City : ${data[key].city}</p>
                <p>Waste Pincode : ${data[key].pincode}</p>

                <div class="user">
                    <div>
                        <label>Request By - ${data[key].username}<label>
                        <img src="${data[key].userDp}"</img>
                    </div>
                    <spam>User Phone - ${data[key].phoneNumber}<spam>
                </div>
            </div>    
            <div class="service-button">
                <button id="prog" class="btn btn-info" value="${data[key].bioRequestId}">On Progress</button><br>
                <button id="complete" class="btn btn-success" value="${data[key].bioRequestId}">Completed</button><br>
                <button id="delete" class="btn btn-danger value="${data[key].bioRequestId}">Delete</button>
                <li style="margin-top:20px; color:${colStatus}; font-size:16px"
                font-family: 'Roboto Condensed', sans-serif;>${status}</li>
            </div>
            `;
            div.innerHTML = content;
            parent.appendChild(div);

            //On progress
            var progress=document.getElementById("prog");
            progress.addEventListener("click",function(){
                 set(ref(database, 'OrganicRequest/' + progress.value), {
                    address:data[progress.value].address,
                    bioRequestId:progress.value,
                    city:data[progress.value].city,
                    phoneNumber:data[progress.value].phoneNumber,
                    pincode:data[progress.value].pincode,
                    postImg:data[progress.value].postImg,
                    status:"On Progress..",
                    type:data[progress.value].type,
                    userDp:data[progress.value].userDp,
                    userId:data[progress.value].userId,
                    username:data[progress.value].username
                  });
                 location.reload();
            });

            //Delete Function
            var btnDelete = document.getElementById("delete");
            btnDelete.addEventListener("click", function() {
                remove(ref(database, 'OrganicRequest/' + btnDelete.value));
                location.reload();
            }, false);

            //Complete Function
            var btnComp = document.getElementById("complete");
            btnComp.addEventListener("click", function() {
                set(ref(database, 'OrganicRequest/' + btnComp.value), {
                    address:data[btnComp.value].address,
                    bioRequestId:btnComp.value,
                    city:data[btnComp.value].city,
                    phoneNumber:data[btnComp.value].phoneNumber,
                    pincode:data[btnComp.value].pincode,
                    postImg:data[btnComp.value].postImg,
                    status:"Waste Collected..",
                    type:data[btnComp.value].type,
                    userDp:data[btnComp.value].userDp,
                    userId:data[btnComp.value].userId,
                    username:data[btnComp.value].username
                  })
                  .then(() => {
                    location.reload();
                  })
                  .catch((error) => {
                    console.log(error);
                  });
            }, false);

            }
      
     } else {
        const parent=document.getElementById("request-container");
        let content=`
            <div class="error"><h4>No Request Pending</h4></div>
        `
        parent.innerHTML=content;
    }
  }).catch((error) => {
console.error(error);
});
