using UnityEngine;
using System.Collections;
using com.shephertz.app42.gaming.multiplayer.client;
using com.shephertz.app42.gaming.multiplayer.client.command;

public class AppWarpHandler : MonoBehaviour {

	public string HOST = "YOUR SERVER ADDRESS HOST";
	public int PORT = 12346;
	public string APP_KEY = "YOUR APP KEY";
	public string ROOM_ID = "YOUR ROOM ID";
	private string USER = "YOUR USERNAME";
	private string AUTH_DATA = "YOUR AUTH DATA";

	public float updateInterval = 0.1f;

	public GameObject remotePrefab;

	private float timer = 0;
	private AppWarpListener listener;
	private bool isGameStarted = false;

	private string log = "";
	
	void Start () {
		WarpClient.initialize (APP_KEY, HOST, PORT);
		listener = GetComponent<AppWarpListener> ();
		WarpClient.GetInstance ().AddChatRequestListener (listener);
		WarpClient.GetInstance ().AddConnectionRequestListener (listener);
		WarpClient.GetInstance ().AddLobbyRequestListener (listener);
		WarpClient.GetInstance ().AddNotificationListener (listener);
		WarpClient.GetInstance ().AddRoomRequestListener (listener);
		WarpClient.GetInstance ().AddTurnBasedRoomRequestListener (listener);
		WarpClient.GetInstance ().AddUpdateRequestListener (listener);
		WarpClient.GetInstance ().AddZoneRequestListener (listener);

		if(Menu.userName == "")
			USER = System.DateTime.UtcNow.Ticks.ToString();
		else
			USER = Menu.userName;

		WarpClient.GetInstance ().Connect (USER, AUTH_DATA);
	}

	void Update () {
		timer -= Time.deltaTime;
		if(timer < 0)
		{
			timer = updateInterval;

			if(isGameStarted == true)
			{
				float[] data_f = new float[6];
				data_f[0] = transform.position.x;
				data_f[1] = transform.position.y;
				data_f[2] = transform.position.z;
				data_f[3] = transform.rotation.eulerAngles.x;
				data_f[4] = transform.rotation.eulerAngles.y;
				data_f[5] = transform.rotation.eulerAngles.z;
				
				int data_len = (sizeof(float)*6) + (USER.Length*sizeof(char));
				byte[] data = new byte[data_len];

				for(int i=0; i<6; ++i)
				{
					byte[] converted = System.BitConverter.GetBytes(data_f[i]);
					if(System.BitConverter.IsLittleEndian)
					{
						System.Array.Reverse(converted);
					}

					for(int j=0; j<sizeof(float); ++j)
					{
						data[i*sizeof(float)+j] = converted[j];
					}
				}

				char[] charUser = USER.ToCharArray();
				int offset = sizeof(float)*6;

				for(int i=0; i<charUser.Length; ++i)
				{
					byte[] converted = System.BitConverter.GetBytes(charUser[i]);
					if(System.BitConverter.IsLittleEndian)
					{
						System.Array.Reverse(converted);
					}
					
					for(int j=0; j<sizeof(char); ++j)
					{
						data[offset+(i*sizeof(char)+j)] = converted[j];
					}
				}

				WarpClient.GetInstance().SendUpdatePeers(data);
			}
		}
	}

	void OnGUI()
	{
		GUI.Label (new Rect (0, 0, Screen.width, Screen.height/3), log);
	}

	void OnApplicationQuit()
	{
		WarpClient.GetInstance().Disconnect();
	}

	public void onConnect(byte result)
	{
		log = "onConnect : " + result + "\n" +  log;
		if(result == WarpResponseResultCode.SUCCESS)
		{
			gameObject.name = USER;
			WarpClient.GetInstance().JoinRoom(ROOM_ID);
		}
	}

	public void onJoinRoom(byte result)
	{
		log = "\nonJoinRoom : " + result + "\n" + log;
		if(result == WarpResponseResultCode.SUCCESS)
		{
			isGameStarted = true;
		}
	}

	public void onUpdateReceived(byte[] data)
	{
		float[] data_f = new float[6];
		for(int i=0; i<6; ++i)
		{
			byte[] converted = new byte[sizeof(float)];
			for(int j=0; j<sizeof(float); ++j)
			{
				converted[j] = data[i*sizeof(float) + j];
			}
			if(System.BitConverter.IsLittleEndian)
				System.Array.Reverse(converted);
			data_f[i] = System.BitConverter.ToSingle(converted, 0);
		}

		string userName = "";
		int offset = sizeof(float)*6;
		int limit = (data.Length - (6*sizeof(float))) / sizeof(char);
		
		for(int i=0; i<limit; ++i)
		{
			byte[] converted = new byte[sizeof(char)];
			for(int j=0; j < sizeof(char); ++j)
			{
				converted[j] = data[offset + (i*sizeof(char)+j)];
			}

			if(System.BitConverter.IsLittleEndian)
				System.Array.Reverse(converted);

			char charUser = System.BitConverter.ToChar(converted, 0);
			userName += charUser;
		}

		if(userName != USER)
		{
			GameObject obj = GameObject.Find(userName);
			if(obj == null)
			{
				Quaternion rot = new Quaternion();
				rot.eulerAngles = new Vector3(data_f[3],data_f[4],data_f[5]);
				Vector3 pos = new Vector3(data_f[0],data_f[1],data_f[2]);
				Object remote = GameObject.Instantiate(remotePrefab, pos , rot);
				remote.name = userName;
				GameObject goR = (GameObject)remote;
				goR.GetComponentInChildren<TextMesh>().text = userName;
				log = "Player entered your Area of Interest : " + userName + "\n" +  log;
			}
			else
			{
				Vector3 pos = new Vector3(data_f[0],data_f[1],data_f[2]);
				Vector3 rot = new Vector3(data_f[3],data_f[4],data_f[5]);
				//obj.transform.position = pos;
				obj.GetComponent<RemoteCharacter>().SetTransform(pos, rot);
			}
		}
	}

	public void onUserLeft(string name)
	{
		GameObject obj = GameObject.Find(name);
		if(obj != null)
		{
			log = "Player left room : " + name + "\n" +  log;
			Destroy(obj);
		}
	}

	public void onChat(string sender, string message)
	{
		com.shephertz.app42.gaming.multiplayer.client.SimpleJSON.JSONNode msg =  com.shephertz.app42.gaming.multiplayer.client.SimpleJSON.JSON.Parse(message);
		if(int.Parse(msg["type"]) == 2)
		{
			log = "Player left your Area of Interest : " + msg["name"] + "\n" +  log;
			GameObject obj = GameObject.Find(msg["name"]);
			if(obj != null)
			{
				Destroy(obj);
			}
		}
	}

}
