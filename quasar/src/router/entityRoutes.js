/**
 * THIS FILE IS HIGHLY MODIFIED BY THE BLUEPRINT
 * DO NOT EDIT.
 *
 **/
import { menuRoutes } from './entities/menuRoutes';
import { menuItemRoutes } from './entities/menuItemRoutes';
import { clearanceRoutes } from './entities/clearanceRoutes';
import { roleRoutes } from './entities/roleRoutes';
export const entityRoutes = [...menuRoutes, ...menuItemRoutes, ...clearanceRoutes, ...roleRoutes, ...[]];
