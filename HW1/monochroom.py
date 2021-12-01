# import pygame module in this program
import os

import pygame

# activate the pygame library .
# initiate pygame and give permission
# to use pygame's functionality.
from sprites import MainLayer

pygame.init()

# define the RGB value
# for white colour
white = (255, 255, 255)

# assigning values to X and Y variable
X = 640
Y = 457

# create the display surface object
# of specific dimension..e(X, Y).
display_surface = pygame.display.set_mode((X, Y))

# set the pygame window name
pygame.display.set_caption('Image')
sprites = pygame.sprite.Group()
MainLayer(sprites)
clock = pygame.time.Clock()
dt = 0
file_num = 0

# Ensure we have somewhere for the frames
try:
    os.makedirs("Snaps")
except OSError:
    pass

# create a surface object, image is drawn on it.
# infinite loop
while True:
    file_num = file_num + 1
    # completely fill the surface object
    # with white colour
    display_surface.fill(white)

    # copying the image surface object
    # to the display surface object at
    # (0, 0) coordinate.
    # display_surface.blit(image, (0, 0))
    # iterate over the list of Event objects
    # that was returned by pygame.event.get() method.
    events = pygame.event.get()
    for event in events:

        # if event object type is QUIT
        # then quitting the pygame
        # and program both.
        if event.type == pygame.QUIT:
            # deactivates the pygame library
            pygame.quit()

            # quit the program.
            quit()

    sprites.update(40)
    sprites.draw(display_surface)
    # Draws the surface object to the screen.
    pygame.display.update()
    # Save every frame
    filename = "Snaps/%04d.png" % file_num
    pygame.image.save(display_surface, filename)
    dt = clock.tick(25)
