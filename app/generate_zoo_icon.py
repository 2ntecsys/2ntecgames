import os
from PIL import Image, ImageDraw, ImageFont

def create_zoo_icon():
    size = 512
    img = Image.new("RGBA", (size, size), (255, 248, 220, 255)) # warm yellow-cream background
    draw = ImageDraw.Draw(img)
    
    # Draw sky
    draw.rectangle([0, 0, size, int(size * 0.45)], fill=(135, 206, 250, 255)) # Sky blue
    
    # Draw sun
    draw.ellipse([380, 40, 470, 130], fill=(255, 215, 0, 255))
    
    # Draw green grass / ground hills
    draw.arc([-100, int(size * 0.35), size + 100, size + 100], start=180, end=360, fill=(76, 175, 80, 255), width=80)
    draw.rectangle([0, int(size * 0.42), size, size], fill=(76, 175, 80, 255))
    
    # Draw colorful zoo wooden sign board at top center
    sign_box = [80, 30, 432, 140]
    draw.rounded_rectangle(sign_box, radius=20, fill=(139, 69, 19, 255), outline=(92, 51, 23, 255), width=6)
    
    # Inner sign board plate (yellow/orange)
    inner_box = [92, 42, 420, 128]
    draw.rounded_rectangle(inner_box, radius=14, fill=(255, 235, 59, 255), outline=(255, 152, 0, 255), width=4)
    
    # Try to load a font, or use default font scaled
    try:
        # standard linux font path if available
        font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 64)
    except:
        font = ImageFont.load_default()
        
    # Draw "ZOO" text on sign board with dark bold color
    text = "ZOO"
    # Get text bounding box for centering
    bbox = draw.textbbox((0, 0), text, font=font)
    tw = bbox[2] - bbox[0]
    th = bbox[3] - bbox[1]
    tx = (size - tw) // 2
    ty = 42 + (128 - 42 - th) // 2 - 4
    
    # Draw text shadow / outline for comic effect
    for dx, dy in [(-3, -3), (3, -3), (-3, 3), (3, 3), (0, -3), (0, 3), (-3, 0), (3, 0)]:
        draw.text((tx + dx, ty + dy), text, fill=(50, 50, 50, 255), font=font)
    draw.text((tx, ty), text, fill=(211, 47, 47, 255), font=font) # Red-orange ZOO text
    
    # Draw cute cartoon animal representations (Lion face on left, Elephant face on right)
    # Lion (Orange circle with mane triangles)
    lion_center = (140, 320)
    for angle in range(0, 360, 45):
        import math
        rad = math.radians(angle)
        mx = int(lion_center[0] + 55 * math.cos(rad))
        my = int(lion_center[1] + 55 * math.sin(rad))
        draw.ellipse([mx-18, my-18, mx+18, my+18], fill=(245, 124, 0, 255))
    draw.ellipse([lion_center[0]-50, lion_center[1]-50, lion_center[0]+50, lion_center[1]+50], fill=(255, 183, 77, 255))
    # Lion eyes and nose
    draw.ellipse([lion_center[0]-22, lion_center[1]-12, lion_center[0]-10, lion_center[1]], fill=(30, 30, 30, 255))
    draw.ellipse([lion_center[0]+10, lion_center[1]-12, lion_center[0]+22, lion_center[1]], fill=(30, 30, 30, 255))
    draw.polygon([
        (lion_center[0]-8, lion_center[1]+4),
        (lion_center[0]+8, lion_center[1]+4),
        (lion_center[0], lion_center[1]+16)
    ], fill=(30, 30, 30, 255))
    
    # Elephant (Blue-grey circle with big ear and trunk)
    el_center = (372, 320)
    # Big ear
    draw.ellipse([el_center[0]+10, el_center[1]-50, el_center[0]+70, el_center[1]+30], fill=(144, 164, 174, 255))
    # Head
    draw.ellipse([el_center[0]-50, el_center[1]-50, el_center[0]+50, el_center[1]+50], fill=(176, 190, 197, 255))
    # Eyes
    draw.ellipse([el_center[0]-20, el_center[1]-12, el_center[0]-10, lion_center[1]-2], fill=(30, 30, 30, 255))
    # Trunk curve
    draw.arc([el_center[0]-15, el_center[1], el_center[0]+35, el_center[1]+70], start=0, end=180, fill=(176, 190, 197, 255), width=16)

    # Save image
    out_path = "app/src/main/res/drawable/img_app_icon_fg.png"
    os.makedirs(os.path.dirname(out_path), exist_ok=True)
    img.save(out_path, "PNG")
    print(f"Successfully generated custom zoo icon at {out_path}")

if __name__ == "__main__":
    create_zoo_icon()
