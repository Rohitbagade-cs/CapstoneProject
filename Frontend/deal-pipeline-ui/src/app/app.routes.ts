import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login';
import { DealsComponent } from './deals/deals';
import { NgModule } from '@angular/core';

export const routes: Routes = [
    // Default route → login
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  // Login page
  {
    path: 'login',component: LoginComponent
  },
  { path: 'deals', component: DealsComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}