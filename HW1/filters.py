import numpy as np
import pygame


def greyscale(surface: pygame.Surface, scale, mask):
    arr = pygame.surfarray.pixels3d(surface)
    mean_arr = np.dot(arr[:, :, :], [0.216, 0.587, 0.144])
    mean_arr3d = mean_arr[..., np.newaxis]
    new_arr = np.repeat(mean_arr3d[:, :, :], 3, axis=2)

    new_arr = arr + np.multiply((new_arr - arr), scale * mask)
    return pygame.surfarray.make_surface(new_arr)

# import pygame
# from pygame.locals import *
#
# pygame.init()
# display = pygame.display.set_mode((320, 240))
#
# background = pygame.image.load("leaves.png").convert_alpha()
# mask = pygame.image.load("mask-fuzzy.png").convert_alpha()
#
# running = True
# while running:
#     for event in pygame.event.get():
#         if event.type == pygame.QUIT:
#             running = False
#     # draw
#     display.fill(Color(255, 0, 255))
#     masked = background.copy()
#     masked.blit(mask, (0, 0), None, pygame.BLEND_RGBA_MULT)
#     display.blit(masked, (0, 0))
#     pygame.display.flip()
# z# https://stackoverflow.com/questions/16880128/pygame-is-there-any-way-to-only-blit-or-update-in-a-mask/16930209
