import pygame
import math
import json

pygame.init()

# Load game state from file
with open('gameState.json') as f:
    data = json.load(f)

WIDTH, HEIGHT = 800, 600
BACKGROUND_COLOR = (13, 154, 214)
HEXAGON_SPACING = 5

screen = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption("catan")

color_map = {
    "Wood": (161, 102, 47),
    "Brick": (227, 118, 25),
    "Wheat": (255, 215, 0),
    "Sheep": (105, 150, 0),
    "Ore": (156, 141, 118),
    "Desert": (210, 180, 140)
}
# Load game state from file
with open('gameState.json') as f:
    data = json.load(f)

# Ensure 'villages' key exists in the JSON data
if "villages" not in data:
    data["villages"] = []  # Initialize as an empty list if not present
if "cities" not in data:
    data["cities"] = []  # Initialize as an empty list if not present
# Button and Menu
menu_button_rect = pygame.Rect(10, 10, 100, 40)
menu_open = False

menu_options = ["Build Village", "Build City", "Build Road"]
menu_option_rects = [pygame.Rect(10, 60 + i * 40, 150, 30) for i in range(len(menu_options))]

selected_option = None  # Track the selected menu option
village_color = (0, 255, 0)  # Color for the village

# Village Data
villages = []  # Store (index, vertex) of villages
occupied_vertices = set()  # Set to store occupied vertices

def hexagon_vertices(x, y, size):
    vertices = []
    for i in range(6):
        angle = math.pi / 3 * i + math.pi / 2 
        vx = x + size * math.cos(angle)
        vy = y + size * math.sin(angle)
        vertices.append((vx, vy))
    return vertices

def draw_hexagon(screen, vertices, color, number, resource):
    pygame.draw.polygon(screen, color, vertices, 0)
    pygame.draw.polygon(screen, (0, 0, 0), vertices, 1)
    
    # Draw resource text
    font = pygame.font.Font(None, 20)
    text_surf = font.render(resource, True, (255, 255, 255))
    text_rect = text_surf.get_rect(center=(sum(v[0] for v in vertices) / 6, (vertices[1][1] + vertices[4][1]) / 2 - 10))
    screen.blit(text_surf, text_rect)

    # Draw number in the center of the hexagon
    number_color = (255, 0, 0) if number in [6, 8] else (255, 255, 255)
    font = pygame.font.Font(None, 24)
    number_surf = font.render(str(number), True, number_color)
    number_rect = number_surf.get_rect(center=(sum(v[0] for v in vertices) / 6, (vertices[1][1] + vertices[4][1]) / 2 + 10))
    screen.blit(number_surf, number_rect)

def draw_port_with_label(screen, x, y, color, angle):
    port_surface = pygame.Surface((20, 20), pygame.SRCALPHA)
    pygame.draw.rect(port_surface, color, (0, 0, 20, 20))
    rotated_port = pygame.transform.rotate(port_surface, angle)
    port_rect = rotated_port.get_rect(center=(x, y + 10))
    screen.blit(rotated_port, port_rect)

def draw_ports(screen, offset_x, offset_y):
    draw_port_with_label(screen, offset_x + 85, offset_y - 44, (227, 118, 25), 30)
    draw_port_with_label(screen, offset_x + 168, offset_y - 40, (255, 255, 255), 60)
    draw_port_with_label(screen, offset_x + 235, offset_y, (156, 141, 118), 60)
    draw_port_with_label(screen, offset_x + 283, offset_y + 75, (161, 102, 47), 0)
    draw_port_with_label(screen, offset_x + 243, offset_y + 150, (105, 150, 0), 30)
    draw_port_with_label(screen, offset_x + 163, offset_y + 200, (255, 255, 255), 30)
    draw_port_with_label(screen, offset_x + 80, offset_y + 197, (255, 215, 0), 60)
    draw_port_with_label(screen, offset_x + 35, offset_y + 120, (255, 255, 255), 0)
    draw_port_with_label(screen, offset_x + 35, offset_y + 35, (255, 255, 255), 0)

def draw_menu(screen, menu_open):
    pygame.draw.rect(screen, (200, 200, 200), menu_button_rect)
    font = pygame.font.Font(None, 36)
    text_surf = font.render("Menu", True, (0, 0, 0))
    screen.blit(text_surf, (menu_button_rect.x + 10, menu_button_rect.y + 5))
    
    if menu_open:
        for i, rect in enumerate(menu_option_rects):
            pygame.draw.rect(screen, (180, 180, 180), rect)
            option_surf = font.render(menu_options[i], True, (0, 0, 0))
            screen.blit(option_surf, (rect.x + 5, rect.y + 5))
def get_adjacent_vertices(hex_index, vertex_index):
    # Return the indices of the adjacent vertices based on the current vertex index
    adjacent = []
    if vertex_index == 0:
        adjacent = [(hex_index, 1), (hex_index, 5)]
    elif vertex_index == 1:
        adjacent = [(hex_index, 0), (hex_index, 2)]
    elif vertex_index == 2:
        adjacent = [(hex_index, 1), (hex_index, 3)]
    elif vertex_index == 3:
        adjacent = [(hex_index, 2), (hex_index, 4)]
    elif vertex_index == 4:
        adjacent = [(hex_index, 3), (hex_index, 5)]
    elif vertex_index == 5:
        adjacent = [(hex_index, 4), (hex_index, 0)]
    return adjacent
# Function to get neighboring hexagon and the corresponding vertex
def get_neighboring_hex_and_vertex(hex_index, vertex_index):
    # Define neighbor hexagon and vertex mapping
    neighboring_hex_map = {
        0: (-1, 3),  # Top-left
        1: (-1, 4),  # Top
        2: (1, 5),   # Bottom-right
        3: (1, 0),   # Bottom
        4: (1, 1),   # Bottom-left
        5: (-1, 2),  # Top-right
    }

    if vertex_index in neighboring_hex_map:
        neighbor_hex_index, neighbor_vertex_index = neighboring_hex_map[vertex_index]
        return (hex_index + neighbor_hex_index, neighbor_vertex_index)
    
    return None  # No neighboring hexagon found for the given vertex

    
    if vertex_index in neighboring_hex_map:
        return neighboring_hex_map[vertex_index]
    
    return None  # No neighboring hexagon found for the given vertex

def draw_village(screen, x, y):
    pygame.draw.circle(screen, village_color, (x, y), 5)  # Draw a circle representing the village

def calculate_bounding_box(hexagons):
    min_x = min(hex['x'] for hex in hexagons)
    max_x = max(hex['x'] for hex in hexagons)
    min_y = min(hex['y'] for hex in hexagons)
    max_y = max(hex['y'] for hex in hexagons)
    return min_x, max_x, min_y, max_y

def is_point_near_vertex(px, py, vertices, threshold=5):
    for vx, vy in vertices:
        if math.hypot(vx - px, vy - py) <= threshold:
            return True
    return False

def is_village_near(px, py, village_data, threshold=5):
    for _, (vx, vy) in village_data:
        if math.hypot(vx - px, vy - py) <= threshold:
            return True
    return False
# Track valid city positions
valid_city_positions = []
def can_place_village(hex_index, vertex_index):
    # Check for existing village in the same hexagon at adjacent vertices
    adjacent_vertices = get_adjacent_vertices(hex_index, vertex_index)
    for adj_hex_index, adj_vertex_index in adjacent_vertices:
        for village in data['villages']:
            if village['hex_index'] == adj_hex_index and village['vertex_index'] == adj_vertex_index:
                print(f"Cannot place village: adjacent village found at hex {adj_hex_index}, vertex {adj_vertex_index}")
                return False

    # Check for existing village in neighboring hexagon
    neighboring_data = get_neighboring_hex_and_vertex(hex_index, vertex_index)
    if neighboring_data:
        neighboring_hex_index, neighboring_vertex_index = neighboring_data
        for village in data['villages']:
            if village['hex_index'] == neighboring_hex_index and village['vertex_index'] == neighboring_vertex_index:
                print(f"Cannot place village: adjacent village found in neighboring hex {neighboring_hex_index}, vertex {neighboring_vertex_index}")
                return False
    
    return True

def add_village(px, py, hex_index, vertices):
    for vertex_index, (vx, vy) in enumerate(vertices):
        if math.hypot(vx - px, vy - py) <= 5:
            if can_place_village(hex_index, vertex_index):
                # Place the village
                occupied_vertices.add((hex_index, (vx, vy)))
                villages.append((hex_index, (vx, vy)))
                valid_city_positions.append((vx, vy))  # Add to valid city positions

                print("Village placed at:", vx, vy)

                # Add village data to JSON structure
                village_data = {
                    "hex_index": hex_index,
                    "vertex_index": vertex_index,
                    "vertex": vertices[vertex_index]
                }
                data["villages"].append(village_data)

                # Save updated game state to JSON file
                with open('gameState.json', 'w') as f:
                    json.dump(data, f, indent=4)

                return True
            else:
                print("Village placement failed due to proximity rules.")
                return False

    return False

def add_city(px, py, hex_index, vertices, village_data):
    for i, village in enumerate(data["villages"]):
        village_hex_index = village["hex_index"]
        village_vertex_index = village["vertex_index"]
        village_vertex = village["vertex"]

        if village_hex_index == hex_index:
            vx, vy = village_vertex
            if math.hypot(vx - px, vy - py) <= 5:
                # Place the city
                occupied_vertices.add((hex_index, (px, py)))
                cities.append((hex_index, (px, py)))
                print("City placed at:", px, py)
                
                # Add city data to JSON structure
                city_data = {
                    "hex_index": hex_index,
                    "vertex_index": village_vertex_index,  # Store the village's vertex index
                    "vertex": (px, py)
                }
                data["cities"].append(city_data)

                # Remove the matched village from JSON data
                data["villages"].pop(i)
                print(f"Village removed from hex {hex_index} at vertex {village_vertex_index}")

                # Save updated game state to JSON file
                with open('gameState.json', 'w') as f:
                    json.dump(data, f, indent=4)

                return True

    print("Cannot place city: No village nearby within 5 pixels.")
    return False

def draw_city(screen, x, y):
    pygame.draw.rect(screen, (0, 255, 0), (x - 10, y - 10, 15, 15))  # Draw a blue square representing the city


min_x, max_x, min_y, max_y = calculate_bounding_box(data['hexagons'])
board_width = max_x - min_x
board_height = max_y - min_y
offset_x = (WIDTH - board_width) // 2 - min_x
offset_y = (HEIGHT - board_height) // 2 - min_y
village_placed = False  # Flag to check if a village has been placed

running=True
# Add a new list to track city data
cities = []  # Store (index, vertex) of cities

# Modify your event loop to handle "Build City" option
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        elif event.type == pygame.MOUSEBUTTONDOWN:
            mouse_x, mouse_y = event.pos
            if menu_button_rect.collidepoint(event.pos):
                menu_open = not menu_open  # Toggle the menu
            elif menu_open:
                for i, rect in enumerate(menu_option_rects):
                    if rect.collidepoint(event.pos):
                        selected_option = menu_options[i]
                        print(f"{selected_option} selected")
                        menu_open = False  # Close the menu after selection
                        village_placed = False  # Reset flag when an option is selected
                        break
            elif selected_option == "Build Village" and not village_placed:
                placed = False
                for idx, hex_data in enumerate(data['hexagons']):
                    x = hex_data['x'] + offset_x
                    y = hex_data['y'] + offset_y
                    size = hex_data['size']
                    vertices = hexagon_vertices(x, y, size)
                    if is_point_near_vertex(mouse_x, mouse_y, vertices):
                        if not is_village_near(mouse_x, mouse_y, occupied_vertices):
                            if add_village(mouse_x, mouse_y, idx, vertices):
                                village_placed = True  # Set flag to indicate a village was placed
                                selected_option = None  # Deselect the option after placing the village
                            else:
                                print("Unable to place village")
                            placed = True
                        break
            elif selected_option == "Build City":
                for idx, hex_data in enumerate(data['hexagons']):
                    x = hex_data['x'] + offset_x
                    y = hex_data['y'] + offset_y
                    size = hex_data['size']
                    vertices = hexagon_vertices(x, y, size)
                    if is_point_near_vertex(mouse_x, mouse_y, vertices):
                        add_city(mouse_x, mouse_y, idx, vertices, occupied_vertices)
                        selected_option = None  # Deselect the option after placing the city
                        break
            
    screen.fill(BACKGROUND_COLOR)
    for hex_data in data['hexagons']:
        x = hex_data['x'] + offset_x
        y = hex_data['y'] + offset_y
        size = hex_data['size']
        resource = hex_data['resource']
        number = hex_data['number']
        vertices = hexagon_vertices(x, y, size)
        color = color_map.get(resource, (0, 0, 0))
        draw_hexagon(screen, vertices, color, number, resource)

    draw_ports(screen, offset_x, offset_y)

    # Draw the villages
    for idx, (vx, vy) in villages:
        draw_village(screen, vx, vy)
    
    # Draw the cities
    for idx, (cx, cy) in cities:
        draw_city(screen, cx, cy)

    draw_menu(screen, menu_open)
    pygame.display.flip()