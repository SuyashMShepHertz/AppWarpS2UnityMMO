using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;
using com.shephertz.app42.gaming.multiplayer.client.listener;
using com.shephertz.app42.gaming.multiplayer.client.events;

public class AppWarpListener : MonoBehaviour, ConnectionRequestListener, ZoneRequestListener, LobbyRequestListener, RoomRequestListener, ChatRequestListener, TurnBasedRoomListener, UpdateRequestListener, NotifyListener {

	private AppWarpHandler appwarpHandler;

	void Start()
	{
		appwarpHandler = GetComponent<AppWarpHandler> ();
	}

	//Connection Request Listener

	// Invoked when a response for Connect request is received.
	public void onConnectDone(ConnectEvent eventObj){
		appwarpHandler.onConnect (eventObj.getResult ());
	}

	// Invoked when a response for Disconnect request is received.
	public void onDisconnectDone(ConnectEvent eventObj){}

	// Invoked when a response for InitUDP request is received.
	public void onInitUDPDone(byte resultCode){}

	//Zone Request Listener
	
	// Invoked when a response for DeleteRoom request is receieved.
	public void onDeleteRoomDone(RoomEvent eventObj){}

	// Invoked when a response for GetAllRooms request is receieved.
	public void onGetAllRoomsDone(AllRoomsEvent eventObj){}

	/// Invoked when a response for CreateRoom request is receieved.
	public void onCreateRoomDone(RoomEvent eventObj){}

	// Invoked when a response for GetOnlineUsers request is receieved.
	public void onGetOnlineUsersDone(AllUsersEvent eventObj){}

	/// Invoked when a response for GetLiveUserInfo request is receieved.
	public void onGetLiveUserInfoDone(LiveUserInfoEvent eventObj){}

	/// Invoked when a response for SetCustomUserData request is receieved.
	public void onSetCustomUserDataDone(LiveUserInfoEvent eventObj){}

	// Invoked when a response from GetRoomWithNUser and GetRoomWithProperties 
	public void onGetMatchedRoomsDone(MatchedRoomsEvent matchedRoomsEvent){}
	
	/// Invoked when a response from invokeZoneRPC is received
	public void onInvokeZoneRPCDone(RPCEvent rpcEvent){}

	//Lobby Request Listener
	
	/// Invoked when the response for joinLobby request is received.
	public void onJoinLobbyDone(LobbyEvent eventObj){}

	/// Invoked when the response for leaveLobby request is received.
	public void onLeaveLobbyDone(LobbyEvent eventObj){}

	/// Invoked when the response for subscribeLobby request is received.
	public void onSubscribeLobbyDone(LobbyEvent eventObj){}

	// Invoked when the response for unsubscribeLobby request is received.
	public void onUnSubscribeLobbyDone(LobbyEvent eventObj){}

	// Invoked when the response for GetLiveLobbyInfo request is received.
	public void onGetLiveLobbyInfoDone(LiveRoomInfoEvent eventObj){}

	//Room Request Listener
	
	/// Invoked when the response for subscribeRoom request is received.
	public void onSubscribeRoomDone(RoomEvent eventObj){}

	/// Invoked when the response for unsubscribeRoom request is received.
	public void onUnSubscribeRoomDone(RoomEvent eventObj){}

	/// Invoked when the response for joinRoom request is received.
	public void onJoinRoomDone(RoomEvent eventObj){
		appwarpHandler.onJoinRoom (eventObj.getResult ());
	}

	/// Invoked when the response for leaveRoom request is received.
	public void onLeaveRoomDone(RoomEvent eventObj){}

	/// Invoked when the response for GetLiveRoomInfo request is received.
	public void onGetLiveRoomInfoDone(LiveRoomInfoEvent eventObj){}

	/// Invoked when the response for setCustomRoomData request is received.
	public void onSetCustomRoomDataDone(LiveRoomInfoEvent eventObj){}

	/// Invoked when the response for UpdateRoomProperties request is received.
	public void onUpdatePropertyDone(LiveRoomInfoEvent lifeLiveRoomInfoEvent){}

	/// Invoked when the response for LockProperties request is received.
	public void onLockPropertiesDone(byte result){}

	// Invoked when the response for UnlockProperties request is received.
	public void onUnlockPropertiesDone(byte result){}

	// Invoked when the response for invokeRoomRPC request is received
	public void onInvokeRoomRPCDone(RPCEvent rpcEvent){}

	//Chat Request Listener

	/// Invoked when a response for sendChat is received. Result of the
	/// operation is passed as an argument of value WarpResponseResultCode.
	public void onSendChatDone(byte result){}

	/// Invoked when a response for sendPrivateChat is received. Result of the
	/// operation is passed as an argument of value WarpResponseResultCode.
	public void onSendPrivateChatDone(byte result){}

	//Turn Based Room Listener
	
	/// Invoked when a response for a sendMove request is received.
	public void onSendMoveDone(byte result){}

	//Invoked when a response for startGame request is received
	public void onStartGameDone(byte result){}

	//Invoked when a response for stopGame request is received
	public void onStopGameDone(byte result){}

	//Invoked when a response for getMoveHistory request is received
	public void onGetMoveHistoryDone(byte result, MoveEvent[] moves){}

	//Update Request Listener
	
	/// Invoked when a response for sendUpdatePeers is received. Result of the
	/// operation is passed as an argument of value WarpResponseResultCode.
	public void onSendUpdateDone(byte result){}

	/// Result of SendPrivateUpdate request. The result value maps to a WarpResponseResultCode
	public void onSendPrivateUpdateDone(byte result){}

	//Notify Listener
	
	/// Invoked when a room is created.
	public void onRoomCreated(RoomData eventObj){}

	/// Invoked when a room is deleted.
	public void onRoomDestroyed(RoomData eventObj){}

	/// Invoked when a user leaves a room.
	public void onUserLeftRoom(RoomData eventObj, String username){
		appwarpHandler.onUserLeft (username);
	}

	/// Invoked when a user joins a room.
	public void onUserJoinedRoom(RoomData eventObj, String username){}

	/// Invoked when a user joins the lobby.
	public void onUserLeftLobby(LobbyData eventObj, String username){}

	/// Invoked when a user joins the lobby.
	public void onUserJoinedLobby(LobbyData eventObj, String username){}   

	/// Invoked when a chat message is sent in one of the subscribed rooms.
	public void onChatReceived(ChatEvent eventObj){
		appwarpHandler.onChat (eventObj.getSender (), eventObj.getMessage ());
	}

	/// Invoked when a updatePeers request is sent in one of the subscribed rooms.
	public void onUpdatePeersReceived(UpdateEvent eventObj){
		appwarpHandler.onUpdateReceived (eventObj.getUpdate ());
	}

	/// Invoked when a updatePeers request is sent in one of the subscribed rooms.
	public void onUserChangeRoomProperty(RoomData roomData, string sender, Dictionary<String, object> properties, Dictionary<String, String> lockedPropertiesTable){}

	/// Invoked when a private chat is received from the given sender.
	public void onPrivateChatReceived(String sender, String message){}

	/// Invoked when a user's move is completed in a turn based room
	public void onMoveCompleted(MoveEvent moveEvent){}

	/// Invoked to indicate that a user has lost connectivity. Subscribers of the users location 
	/// will receive this.
	public void onUserPaused(String locid, Boolean isLobby, String username){}

	/// Invoked when a user's connectivity is restored. Subscribers of the users location 
	/// will receive this.
	public void onUserResumed(String locid, Boolean isLobby, String username){}

	/// Invoked when a user's start game in a turn based room
	public void onGameStarted(String sender, String roomId, String nextTurn){}

	///  Invoked when a user's stop game in a turn based room
	public void onGameStopped(String sender, String roomId){}

	/// Invoked when a private update message is received
	public void onPrivateUpdateReceived(String sender, byte[] update, bool fromUdp){}
}
