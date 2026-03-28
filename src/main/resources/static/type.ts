import type { ApiResponse, PaginatedResponse } from "./api.type";
import type { Organization } from "./organization.type";

export interface Pet {
  petId: string;
  name: string;
  species: string;
  breed: string;
  age: number; // Age in months
  ageDisplay: string;
  vaccinated: boolean;
  gender: PetGender;
  status: PetStatus;
  healthStatus: PetHealthStatus;
  organization: Pick<Organization, "organizationId" | "name">;
  province: string;
  provinceCode: number;
  ward: string;
  wardCode: number;

  // omitted fields
  color: string;
  weight: 0;
  description: string;
  neutered: boolean;
  rescueDate: string;
  rescueLocation: string;
  imageUrls: string[];
  shelterId: string;
  createdAt: string;
  updatedAt: string;
}
// create pet
export type CreatePetRequest = Omit<
    Pet,
    | "petId"
    | "color"
    | "weight"
    | "description"
    | "neutered"
    | "rescueDate"
    | "rescueLocation"
    | "imageUrls"
    | "shelterId"
    | "createdAt"
    | "updatedAt"
;

export type CreatePetResponse = ApiResponse<Pet>;
// get all res
export type GetAllPetsResponse = PaginatedResponse< Omit<
  Pet,
  | "color"
  | "weight"
  | "description"
  | "neutered"
  | "rescueDate"
  | "rescueLocation"
  | "imageUrls"
  | "shelterId"
  | "createdAt"
  | "updatedAt"
& {

  imageUrl: string;
}>;

// update pet
export type UpdatePetRequest = Omit<
    Pet,
    | "petId"
    | "rescueDate"
    | "rescueLocation"
;

  
export type PetGender = "MALE" | "FEMALE";
export type PetStatus =
  | "ADOPTED"
  | "UNOWNED"
  | "PENDING"
  | "FOSTERED"
  | "UNAVAILABLE";
export type PetHealthStatus =
  | "HEALTHY"
  | "UNDER_TREATMENT"
  | "RECOVERING"
  | "SPECIAL_NEEDS";