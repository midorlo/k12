export const roleRoutes = [
  {
    path: '/roles',
    meta: { public: false },
    component: () => import('pages/entities/role/Roles.vue'),
  },
  {
    path: '/roles/new',
    meta: { public: false },
    component: () => import('pages/entities/role/RoleEdit.vue'),
  },
  {
    path: '/roles/:id',
    meta: { public: false },
    component: () => import('pages/entities/role/RoleView.vue'),
  },
  {
    path: '/roles/:id/edit',
    meta: { public: false },
    component: () => import('pages/entities/role/RoleEdit.vue'),
  },
];
