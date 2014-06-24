using UnityEngine;
using System.Collections;

public class RemoteCharacter : MonoBehaviour {

	public float gravity;
	private Vector3 position;
	private Quaternion rotation;
	private bool started;

	private CharacterController charController;
	private Animation anim;

	void Start () {
		started = false;
		position = new Vector3 ();
		rotation = new Quaternion ();
		charController = GetComponent<CharacterController> ();
		anim = GetComponent<Animation> ();
	}

	void Update () {
		if(started == true){
			transform.position = Vector3.Lerp(transform.position, position, Time.deltaTime);
			transform.rotation = Quaternion.Lerp(transform.rotation, rotation, Time.deltaTime);
		}

		if(!charController.isGrounded)
		{
			Vector3 moveDir = transform.TransformDirection (Vector3.forward);
			moveDir.y -= gravity * Time.deltaTime;
			charController.Move(moveDir * Time.deltaTime);
		}

		float dist = Vector3.Distance (transform.position, position);
		if (dist > 0.3f)
			anim.CrossFade ("walk", 0.2f);
		else
			anim.CrossFade ("idle",0.2f);
	}

	public void SetTransform(Vector3 pos, Vector3 rot){
		started = true;
		position = pos;
		rotation.eulerAngles = rot;
	}
}
