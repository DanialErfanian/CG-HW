import pygame

from filters import greyscale


class MainLayer(pygame.sprite.Sprite):
    T_seconds = 15
    time = 0

    def __init__(self, *args):
        super().__init__(*args)
        surface = pygame.display.get_surface()
        surface.fill((100, 100, 100))
        self.rect = surface.get_rect()
        self.image_copy = pygame.image.load(r'assets/img.jpg').convert_alpha()
        self.image = self.image_copy.copy()

        self.mask = pygame.surfarray.pixels3d(pygame.image.load(r'assets/mask.png')) / 255

        pygame.mixer.init()
        pygame.mixer.music.load("assets/music.mp3")
        pygame.mixer.music.play(-1, 0.0)

    def update(self, dt):
        print(dt, self.time)
        self.time += dt / 1000
        scale = min(1, self.time / self.T_seconds)
        self.image = greyscale(self.image_copy, scale, self.mask)
        myfont = pygame.font.SysFont("monospace", 15)

        # render text
        label = myfont.render("Danial Products :)", 3, (19, 171, 59))
        self.image.blit(label, (350, 420))

        # self.image.blit(self.mask, (0, 0), None, pygame.BLEND_RGBA_MULT)
        # self.image = image
        # self.image.set_alpha(min(255, int(256 * scale)))
        if self.time >= self.T_seconds:
            pygame.quit()

            # quit the program.
            quit()
