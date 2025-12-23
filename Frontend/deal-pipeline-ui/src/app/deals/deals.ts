import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { DealsService } from '../services/deals';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth';
import { filter } from 'rxjs';

@Component({
  selector: 'app-deals',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './deals.html',
  styleUrl: './deals.css',
})
export class DealsComponent implements OnInit {

  deals: any[] = [];
  loading = true;
  error = '';

  // 🔹 Track edit mode
  editingDealId: number | null = null;

  isAdmin = false;
  isAnalyst = false;

  // 🔹 Form model (MATCHES backend DTO)
  newDeal = {
    companyName: '',
    dealAmount: 0,
    description: '',
    statusId: 1,          // NEW
    assignedToUserId: 2   // admin
  };

  constructor(
    private router: Router,
    private dealsService: DealsService,
    public authService: AuthService
  ) {}

  // ==================================================
  // INIT
  // ==================================================
  ngOnInit(): void {
    this.isAdmin = this.authService.isAdmin();
    this.isAnalyst = this.authService.isAnalyst();
    this.fetchDeals();
  }


  // ==================================================
  // FETCH DEALS
  // ==================================================
  fetchDeals() {
    this.loading = true;

    this.dealsService.getDeals().subscribe({
      next: (data) => {
        this.deals = data;
        this.loading = false;
        console.log('Deals loaded:', data);
      },
      error: (err) => {
        console.error(err);
        this.error = 'Failed to load deals';
        this.loading = false;
      }
    });
  }

  // ==================================================
  // CREATE or UPDATE DEAL
  // ==================================================
  saveDeal() {
    const payload: any = {
      companyName: this.newDeal.companyName,
      //dealAmount: Number(this.newDeal.dealAmount),
      description: this.newDeal.description,
      statusId: this.newDeal.statusId,
      assignedToUserId: this.newDeal.assignedToUserId
    };
      // 🔐 Only ADMIN can send dealAmount
    if (this.isAdmin) {
      payload.dealAmount = Number(this.newDeal.dealAmount);
    }

    console.log('Final payload:', payload);

    if (this.editingDealId) {
      // 🔹 UPDATE
      this.dealsService.updateDeal(this.editingDealId, payload).subscribe({
        next: () => {
          alert('Deal updated ✅');
          this.fetchDeals();
          this.resetForm();
        },
        error: (err) => {
          console.error(err);
          alert('Update failed ❌');
        }
      });
    } else {
      // 🔹 CREATE
      this.dealsService.createDeal(payload).subscribe({
        next: () => {
          alert('Deal created successfully ✅');
          this.fetchDeals();
          this.resetForm();
        },
        error: (err) => {
          console.error(err);
          alert('Create failed ❌');
        }
      });
    }
  }

  // ==================================================
  // EDIT DEAL
  // ==================================================
  editDeal(deal: any) {
    this.editingDealId = deal.id;
    this.newDeal = {
      companyName: deal.companyName,
      dealAmount: deal.dealAmount,
      description: deal.description,
      statusId: this.getStatusId(deal.status) || 1,
      assignedToUserId: 2
    };
  }

  // ==================================================
  // DELETE DEAL
  // ==================================================
  deleteDeal(id: number) {
  if (!this.isAdmin) return;
  if (!confirm('Are you sure you want to delete this deal?')) {
    return;
  }

  this.dealsService.deleteDeal(id).subscribe({
    next: () => {
      alert('Deal deleted successfully ✅');
      this.fetchDeals(); // refresh list
    },
    error: (err) => {
      console.error(err);
      alert('Delete failed ❌');
    }
  });
}


  // ==================================================
  // RESET FORM
  // ==================================================
  resetForm() {
    this.newDeal = {
      companyName: '',
      dealAmount: 0,
      description: '',
      statusId: 1,
      assignedToUserId: 2
    };
    this.editingDealId = null;
  }

  // ==================================================
  // MAP STATUS STRING → DB ID
  // ==================================================
  getStatusId(status: string): number {
    switch (status) {
      case 'NEW': return 1;
      case 'IN_REVIEW': return 2;
      case 'APPROVED': return 3;
      case 'REJECTED': return 4;
      case 'CLOSED': return 5;
      default: return 1;
    }
  }

  // ==================================================
  // LOGOUT
  // ==================================================
  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
  
}
