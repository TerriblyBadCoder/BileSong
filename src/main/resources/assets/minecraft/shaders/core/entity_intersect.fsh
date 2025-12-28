#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:light.glsl>
uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform sampler2D Sampler2;
uniform sampler2D Sampler3;
uniform sampler2D Sampler4;
uniform sampler2D Sampler5;
uniform sampler2D Sampler6;
uniform sampler2D Sampler7;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;

out vec4 fragColor;
const float near = 0.01;
const float far = 100.0;
float LinearizeDepth(float depth)
{
    float z = depth * 2.0f - 1.0f;
    return (near * far) / (far + near - z * (far - near));
}
void main() {
    float animation = GameTime * 3000.0;
    ivec2 uvd = ivec2(gl_FragCoord.xy);
    vec2 uvd2 = vec2(uvd.x,uvd.y)/ScreenSize;
    vec2 ScreenSized = ScreenSize/8.0;
    float depth = texture(Sampler0,uvd2).x;
    //vec3 ndc = vec3(SCREEN_UV, depth) * 2.0 - 1.0;
    vec3 ndc = vec3(uvd2 * 2.0 - 1.0, depth);
    vec4 view = inverse(ProjMat) * vec4(ndc, 1.0);
    view.xyz /= view.w;
    float linear_depth = -view.z;


    float object_depth = gl_FragCoord.z;
    vec3 object_ndc = vec3(uvd2 * 2.0 - 1.0, object_depth);
    vec4 object_view = inverse(ProjMat) * vec4(object_ndc, 1.0);
    object_view.xyz /= object_view.w;
    float linear_object_depth = -object_view.z;
    float fresnel = pow(1.0 - dot(normalize(gl_FragCoord.xyz), Light0_Direction), 1.0);
    vec4 vertexColorCopy = vertexColor;
    vertexColorCopy.a*=2.0;
    vec4 color = vertexColorCopy*vec4(1,1,1,0.7)*overlayColor;
    color.a=min(1.0,color.a);
    float sinusd = sin(object_view.x*1.2+animation)/10.0+cos(object_view.z*1.2-animation)/10.0+sin(object_view.y*1.2-animation*0.8)/10.0;
    if(abs(linear_depth-linear_object_depth)<0.3+sinusd/3.2){
        color = vec4(0.9,1.0,0.9,1.0);
    }


    //vec4 color =vec4(vec3(1.0,1.0,1.0)*dist,1.0);

    if (color.a < 0.01) {
        discard;
    }

    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
