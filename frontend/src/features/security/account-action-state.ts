export type AccountActionState = {
  status: "idle" | "success" | "error";
  message?: string;
  updatedEmail?: string;
};

export const INITIAL_ACCOUNT_ACTION_STATE: AccountActionState = {
  status: "idle"
};
