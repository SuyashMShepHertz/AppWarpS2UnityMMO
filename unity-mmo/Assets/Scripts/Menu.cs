using UnityEngine;
using System.Collections;

public class Menu : MonoBehaviour {

	public static string userName = "";
	private string name = "";

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	void OnGUI()
	{
		GUI.Label (new Rect (10, 10, Screen.width - 10 * 2, 32), "Enter a name :");
		name = GUI.TextField (new Rect(10,52,Screen.width - 10*2, 32), name);
		if(GUI.Button(new Rect(Screen.width/2 - 128/2,94, 128, 32), "Let's Start"))
		{
			if(name != "")
			{
				Menu.userName = name;
				Application.LoadLevel("scene1");
			}
		}
	}
}
