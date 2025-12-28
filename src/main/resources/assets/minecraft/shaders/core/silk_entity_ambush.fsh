#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictestuniforms.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:projection.glsl>
uniform sampler2D Sampler0;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec3 magic = vec3(0.05711056f, 0.00083715f, 52.9829189f);
    float animation = GameTime * 4000.0;
    ivec2 uvd = ivec2(gl_FragCoord.xy);
    vec2 uvd2 = vec2(uvd.x,uvd.y)/ScreenSize;
    vec4 color = texture(Sampler0, texCoord0);
    //new
    float object_depth = gl_FragCoord.z;
    vec3 object_ndc = vec3(uvd2 * 2.0 - 1.0, object_depth);
    vec4 object_view = inverse(ProjMat) * vec4(object_ndc, 1.0);
    object_view.xyz /= object_view.w;
    float linear_object_depth = -object_view.z/12.0;
    color.a*=clamp(1.5-linear_object_depth,0.0,1.0);
    uvd.x-=uvd.x%4;
    uvd.y-=uvd.y%4;
    float fracted = fract(magic.z * fract(dot(uvd.xy, magic.xy)));
    if(color.a < fracted||color.a<0.02){
       discard;
    }
    else{
        color.a = 1.0;
    }
    //
#ifdef ALPHA_CUTOUT
    if (color.a < ALPHA_CUTOUT) {
        discard;
    }
#endif
    color *= vertexColor * ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color*=lightMapColor;
    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
