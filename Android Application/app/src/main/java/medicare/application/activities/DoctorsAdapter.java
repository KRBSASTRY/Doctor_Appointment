//package medicare.application.adapters;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.List;
//
//import medicare.application.R;
//
//public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {
//
//    private List<JSONObject> doctorsList;
//
//    public DoctorAdapter(List<JSONObject> doctorsList) {
//        this.doctorsList = doctorsList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        try {
//            JSONObject doctor = doctorsList.get(position);
//            String name = doctor.getString("name");
//            int experience = doctor.getInt("experience");
//            String specialization = doctor.getString("specialization");
//            int consultationFee = doctor.getInt("consultationFee");
//
//            String doctorDetails = "Name: " + name + "\nExperience: " + experience + " years\nSpecialization: " + specialization + "\nConsultation Fee: $" + consultationFee;
//            holder.txtDoctorDetails.setText(doctorDetails);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return doctorsList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView txtDoctorDetails;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            txtDoctorDetails = itemView.findViewById(R.id.txtDoctorDetails);
//        }
//    }
//}
