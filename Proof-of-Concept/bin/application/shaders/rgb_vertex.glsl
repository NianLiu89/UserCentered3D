#version 400 core

in vec3 position;
in vec3 color;

out vec3 pass_color;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){

	gl_Position = transformationMatrix * vec4(position, 1.0);
	pass_color = color;

}