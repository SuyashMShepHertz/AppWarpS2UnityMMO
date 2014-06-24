using UnityEngine;
using System.Collections;

public class Character : MonoBehaviour {
	
	public float speed = 6.0f;
	public float gravity = 20.0f;
	public float rotateSpeed = 3.0f;

	private CharacterController controller;
	private Animation anim;

	void Start()
	{
		controller = GetComponent<CharacterController>();
		anim = GetComponent<Animation> ();
	}

	void Update() {
		Vector3 moveDirection = Vector3.zero;

        if (controller.isGrounded) {			
			transform.Rotate(0, Input.GetAxis("Horizontal") * rotateSpeed, 0);

			moveDirection = transform.TransformDirection(Vector3.forward);
			moveDirection *= speed * Input.GetAxis("Vertical") ;

			if(Input.GetAxis("Vertical") != 0.0f)
				anim.CrossFade("walk",0.2f);
			else
				anim.CrossFade("idle",0.2f);
            
        }
        moveDirection.y -= gravity * Time.deltaTime;
		controller.Move(moveDirection * Time.deltaTime);
	}
}
