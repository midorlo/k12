export const menuItemRoutes = [
  {
    path: '/menu-items',
    meta: { public: false },
    component: () => import('pages/entities/menu-item/MenuItems.vue'),
  },
  {
    path: '/menu-items/new',
    meta: { public: false },
    component: () => import('pages/entities/menu-item/MenuItemEdit.vue'),
  },
  {
    path: '/menu-items/:id',
    meta: { public: false },
    component: () => import('pages/entities/menu-item/MenuItemView.vue'),
  },
  {
    path: '/menu-items/:id/edit',
    meta: { public: false },
    component: () => import('pages/entities/menu-item/MenuItemEdit.vue'),
  },
];
