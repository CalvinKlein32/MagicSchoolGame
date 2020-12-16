const { futimesSync } = require('fs');

var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players=[];
var lobbyNum=1;
var numPlayersInLobby=1;
var host="localhost";

server.listen(8080,host,function(){
    console.log("Server is now running")
});

io.on('connection', function(socket){
    console.log("Player Connected!");
    socket.emit('socketID', {id: socket.id})
    socket.emit('getPlayers',players);
    //socket.broadcast.emit('newPlayer', {id: socket.id});
    socket.emit('newPlayer', {playersInLobby: numPlayersInLobby, lobby:lobbyNum});
    socket.on('gameEnded',function(data){
        socket.broadcast.emit('gameEnded',data);
    });
    socket.on('playerCompletedLevel',function(data){
        socket.broadcast.emit('playerCompletedLevel',data);
    });
    socket.on('playerMoved', function(data){
        data.id=socket.id
        socket.broadcast.emit('playerMoved', data);
        for (var i=0; i<players.length;i++){

           // console.log("player moved"+data.x+data.y);
            if (players[i].id==data.id){
                players[i].x=data.x;
                players[i].y=data.y;
            }
        }
    });
    socket.on('disconnect',function(){
        console.log("Player Disconnected!");
        socket.broadcast.emit('playerDisconeccted',{id: socket.id});
        for (var i=0; i< players.length;i++){
            if (players[i].id == socket.id){
                players.splice(i,1);
            }
        }
    });
    if (numPlayersInLobby<2){
        numPlayersInLobby++;
    }else{
        lobbyNum++;
        numPlayersInLobby=1;

    }
    players.push(new player(socket.id,0,0,lobbyNum));
})

function player(id,x,y,lobby){
    this.id=id;
    this.x=x;
    this.y=y;
    this.lobby=lobby;
}