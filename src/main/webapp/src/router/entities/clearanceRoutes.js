export const clearanceRoutes = [
  {
    path: '/clearances',
    meta: { public: false },
    component: () => import('pages/entities/clearance/Clearances.vue'),
  },
  {
    path: '/clearances/new',
    meta: { public: false },
    component: () => import('pages/entities/clearance/ClearanceEdit.vue'),
  },
  {
    path: '/clearances/:id',
    meta: { public: false },
    component: () => import('pages/entities/clearance/ClearanceView.vue'),
  },
  {
    path: '/clearances/:id/edit',
    meta: { public: false },
    component: () => import('pages/entities/clearance/ClearanceEdit.vue'),
  },
];
