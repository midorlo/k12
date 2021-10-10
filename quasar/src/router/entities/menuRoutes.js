export const menuRoutes = [
  {
    path: '/menus',
    meta: { public: false },
    component: () => import('pages/entities/menu/Menus.vue'),
  },
  {
    path: '/menus/new',
    meta: { public: false },
    component: () => import('pages/entities/menu/MenuEdit.vue'),
  },
  {
    path: '/menus/:id',
    meta: { public: false },
    component: () => import('pages/entities/menu/MenuView.vue'),
  },
  {
    path: '/menus/:id/edit',
    meta: { public: false },
    component: () => import('pages/entities/menu/MenuEdit.vue'),
  },
];
